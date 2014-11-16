using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace StagePlay_EbookParser
{
    class Program
    {
        static private int MAX_DIALOGUE_LENGTH = 140; // Can be turned into an argument that's passed to the program 
        static private int MAX_NARRATIVE_LENGTH = 2 * MAX_DIALOGUE_LENGTH; // This can take up a whole screen, so twice as long

        static void Main(string[] args)
        {
            var fileName = args[0];
            if (!File.Exists(fileName))
            {
                Console.WriteLine("File {0} does not exist or is unreadable. Exiting...", fileName);
                Environment.Exit(1);
            }

            // Stage 1 - Read chunks of text from plain text file
            var chunks = ReadInChunksFromFile(fileName);

            IDictionary<string, int> speakers;
            // Stage 2 - Parse out speakers, remove L. R. C. L.C. and R.C., format brackets, reformat Exit and Enter
            var rawDialogues = ConvertChunksToRawDialogueSections(chunks, out speakers);

            // Stage 3 - Split long raw dialogues into multiple raw chunked dialogues 
            var rawChunkedDialogues = ChunkLongDialogues(rawDialogues);

            // Stage 4 - Add all dialogues and assign numbers and write them out to file
            var finalDialogues = AssignActorBasedSequentialIds(speakers, rawChunkedDialogues);

            
            var playFile = Path.GetDirectoryName(fileName) + Path.DirectorySeparatorChar + Path.GetFileNameWithoutExtension(fileName) + ".playfile";
            OutputFinalDialogues(playFile, finalDialogues);
        }

        static List<string> ReadInChunksFromFile(string fileName)
        {
            var result = new List<string>();

            using (TextReader reader = new StreamReader(fileName))
            {
                String buffer;
                String chunk = String.Empty;
                while (reader.Peek()!=-1)
                {
                    buffer = reader.ReadLine();
                    if (!String.IsNullOrWhiteSpace(buffer))
                    {
                        chunk += " " + buffer;
                    }
                    else
                    {
                        if(!String.IsNullOrWhiteSpace(chunk))
                            result.Add(chunk);
                        chunk = String.Empty;
                    }
                }
            }

            Console.WriteLine("Total chunks: {0}", result.Count);

            return result;
        }

        static List<RawDialogue> ConvertChunksToRawDialogueSections(List<string> linesOfText, out IDictionary<string, int> speakers)
        {
            var result = new List<RawDialogue>();
            speakers = new Dictionary<string, int>();
            var twoSpaces = "  ";

            foreach (var lineOfText in linesOfText)
            {
                var speaker = String.Empty;
                var dialogue = String.Empty;

                var twoSpacesIndex = lineOfText.IndexOf(twoSpaces);
                if (twoSpacesIndex > 0)
                {
                    // It may or may not be a dialogue ... so we have to test and parse out the speaker
                    var possibleSpeaker = lineOfText.Substring(0, twoSpacesIndex);
                    if (HasSpeakerFormat(possibleSpeaker))
                    {
                        speaker = possibleSpeaker.Trim();
                        dialogue = lineOfText.Substring(twoSpacesIndex + 2).Trim();

                        if (!speakers.ContainsKey(speaker))
                        {
                            speakers[speaker] = 0;
                        }
                    }
                    else
                    {
                        dialogue = lineOfText.Trim();
                    }
                }
                else
                {
                    dialogue = lineOfText.Trim();
                }

                //dialogue = dialogue.Replace("L.C.", "Left Center");
                //dialogue = dialogue.Replace("R.C.", "Right Center" );
                //dialogue = dialogue.Replace(" L.", " Left" );
                //dialogue = dialogue.Replace(" R.", " Right");
                //dialogue = dialogue.Replace(" C.", " Center");
                //dialogue = dialogue.Replace("[]", string.Empty);

                dialogue = dialogue.Replace('[', '(');
                dialogue = dialogue.Replace(']', ')');

                var rawDialogue = new RawDialogue(speaker, dialogue);
                result.Add(rawDialogue);
            }

            Console.WriteLine("Following {0} speakers were parsed out...", speakers.Keys.Count());
            foreach (var key in speakers.Keys)
            {
                Console.WriteLine(key);
            }

            Console.WriteLine();
            Console.WriteLine("Total raw dialogues: {0}", result.Count);

            return result;
        }

        static List<RawDialogue> ChunkLongDialogues(List<RawDialogue> rawDialogues)
        {
            var result = new List<RawDialogue>();

            foreach (var rawDialogue in rawDialogues)
            {
                if (rawDialogue.Text.Length < MAX_DIALOGUE_LENGTH)
                {
                    result.Add(rawDialogue);
                }
                else
                {
                    var dialogueChunks = new List<String>();
                    if (!String.IsNullOrEmpty(rawDialogue.Speaker))
                        dialogueChunks = ChunkDialogueText(rawDialogue.Text, MAX_DIALOGUE_LENGTH);
                    else
                        dialogueChunks = ChunkDialogueText(rawDialogue.Text, MAX_NARRATIVE_LENGTH);

                    foreach(var dialogueChunk in dialogueChunks)
                    {
                        result.Add(new RawDialogue(rawDialogue.Speaker, dialogueChunk));
                    }
                }
            }

            return result;
        }

        static List<RawDialogue> AssignActorBasedSequentialIds(IDictionary<string, int> speakers, List<RawDialogue> rawDialogues)
        {
            foreach (var rawDialogue in rawDialogues)
            {
                var speaker = rawDialogue.Speaker;
                if (String.IsNullOrEmpty(speaker)) continue;
                speakers[speaker]++;
                rawDialogue.Id = speakers[speaker];
            }

            return rawDialogues;
        }

        static void OutputFinalDialogues(string playFile, List<RawDialogue> finalDialogues)
        {
            int count = 0;
            using (var outFile = new StreamWriter(playFile, false, Encoding.UTF8))
            {
                foreach (var dialogue in finalDialogues)
                {
                    count++;
                    var output = String.Format("{0}|{1}|{2}|{3}", count, dialogue.Id, dialogue.Speaker, dialogue.Text);
                    outFile.WriteLine(output);
                }
            }
        }

        static private List<String> ChunkDialogueText(string s, int length)
        {
            var result = new List<String>();

            while (true)
            {
                if (s == null) break;
                if (s.Length <= length)
                {
                    result.Add(s);
                    break;
                }
                else
                {
                    for (var i = length; i > 0; i--)
                    {
                        if (s[i] == ' ')
                        {
                            result.Add(s.Substring(0, i));
                            s = s.Substring(i + 1);
                            break;
                        }
                    }
                }
            }

            return result;
        }

        static private bool HasSpeakerFormat(string text)
        {
            if (String.IsNullOrWhiteSpace(text)) return false;
            
            for (int i = 0; i < text.Length; i++)
            {
                char c = text[i];
                
                switch(c)
                {
                    case ' ':
                    case '.':
                        continue;
                    default:
                        if (!Char.IsUpper(c))
                            return false;
                        break;
                }
            }

            return true;
        }

    }

    public class RawDialogue
    {
        public readonly string Speaker;
        public readonly string Text;
        
        public int Id { get; set; }

        public RawDialogue(string speaker, string text)
        {
            Speaker = speaker;
            Text = text;
        }
    }

}

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal static class OpenSSL
    {
        public static Key GenerateKey(string algName)
        {
            int new_id = Database.GetNewId("Key");
            Key newKey = null;

            string result = "";
            if (algName == "RSA")
            {
                result = execute("openssl genpkey -algorithm RSA -outform PEM");
                newKey = new Key(new_id, result, "");
            }
            // Symetric
            else if (algName == "AES")
            {
                result = execute("openssl rand -base64 256");
                newKey = new Key(new_id, result, result);
            }


            return newKey;
        }

        public static string EncryptFile(File file)
        {
            Key key = Database.GetKeys()[file.IdKey];

            string cryptedFileName = file.Name + "(crypted)";

            string command = "openssl enc -aes-256-cbc -salt -in Files/" + file.Name + ".txt -out Files/" + cryptedFileName + ".txt -k " + key.EncryptKey;

            string result = execute(command);
            Debug.WriteLine(result);

            return cryptedFileName;
        }

        public static string DecryptFile(File file)
        {
            Key key = Database.GetKeys()[file.IdKey];

            string decryptedFileName = file.Name + "(decrypted)"; 

            string command = "openssl enc -d -aes-256-cbc -in Files/" + file.Name + ".txt -out Files/" + decryptedFileName + ".txt -k " + key.DecryptKey;
            string result = execute(command);
            Debug.WriteLine(result);

            return decryptedFileName;
        }

        private static string execute(string command)
        {
            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.FileName = "cmd.exe";
            startInfo.Arguments = "/c " + command;
            startInfo.RedirectStandardOutput = true;
            startInfo.UseShellExecute = false;


            using (Process process = Process.Start(startInfo))
            {

                using (System.IO.StreamReader reader = process.StandardOutput)
                {
                    string result = reader.ReadToEnd();
                    //Console.WriteLine("Rezultat: " + result);
                    return result;
                }
            }
        }
    }
}

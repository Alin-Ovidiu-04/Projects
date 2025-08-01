using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Decodeur
{
    internal static class Codec
    {
        public static File Encode(File file)
        {
            string cryptedFileName;

            cryptedFileName = OpenSSL.EncryptFile(file);

            int newId = Database.GetNewId("File");

            // add to database
            File cryptedFile = new File(newId, cryptedFileName, false, file.IdAlg, file.IdKey);
            Database.AddFile(cryptedFile);

            return null;
        }

        public static File Decode(File file)
        {
            string decryptedFileName;

            decryptedFileName = OpenSSL.DecryptFile(file);

            int newId = Database.GetNewId("File");

            // add to database
            File decryptedFile = new File(newId, decryptedFileName, true, file.IdAlg, file.IdKey);
            Database.AddFile(decryptedFile);

            return null;
        }
    }
}

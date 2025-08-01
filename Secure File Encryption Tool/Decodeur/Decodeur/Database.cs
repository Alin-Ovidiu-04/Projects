using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Decodeur
{
    internal static class Database
    {
        private static SqlConnection con;

        public static void Init()
        {
            // connect to database
            con = new SqlConnection();
            string folderPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Database1.mdf");
            con.ConnectionString = "Data Source=(LocalDB)\\MSSQLLocalDB;AttachDbFilename=" + folderPath + ";Integrated Security=True";
        }

        public static Dictionary<int, File> GetFiles()
        {
            Dictionary<int, File> files = new Dictionary<int, File>();

            try
            {
                con.Open();

                string sqlQuery = "SELECT id, name, is_decrypted, id_alg, id_key FROM [File]";
                SqlCommand command = new SqlCommand(sqlQuery, con);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int id = (int)reader["id"];
                    string name = reader["name"].ToString();
                    bool is_decrypted = (bool)reader["is_decrypted"];
                    int id_alg = (int)reader["id_alg"];
                    int id_key = (int)reader["id_key"];

                    // add file to dictionary
                    files[id] = new File(id, name, is_decrypted, id_alg, id_key);
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return files;
        }

        public static List<string> getAllFiles(bool type)
        {
            Dictionary<int, File> Files = GetFiles();
            List<string> filesName = new List<string>();

            for (int i = 1; i <= Files.Count; i++)
            {
                if (Files[i].IsDecrypted == type)
                {
                    Console.WriteLine(Files[i].Name);
                    filesName.Add(Files[i].Name);
                    
                }
            }

            return filesName;
        }

        public static string GetFile( int ID )
        {
            string file = null;

            try
            {
                con.Open();

                string sqlQuery = "SELECT name FROM [File] WHERE id = @ID";
                SqlCommand command = new SqlCommand(sqlQuery, con);
                command.Parameters.AddWithValue("@ID", ID);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    
                    string name = reader["name"].ToString();

                    // add file to dictionary
                    file = name;
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return file;
        }

        public static void AddFile(File file)
        {
            try
            {
                con.Open();

                string commandString;
                if (file.IdAlg == 0 || file.IdKey == 0)
                {
                    commandString = "INSERT INTO [File] VALUES (\'" +
                        file.Id + "\', \'" + file.Name + "\', \'" +
                        file.IsDecrypted + "\', NULL, NULL')";
                }
                else
                {
                    commandString = "INSERT INTO [File] VALUES (\'" +
                        file.Id + "\', \'" + file.Name + "\', \'" +
                        file.IsDecrypted + "\', \'" +
                        file.IdAlg + "\', \'" + file.IdKey + "\')";
                }

                SqlCommand command = new SqlCommand(commandString, con);
                command.ExecuteNonQuery();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }
        }

        public static Dictionary<int, Algorithm> GetAlgorithms()
        {
            Dictionary<int, Algorithm> algorithms = new Dictionary<int, Algorithm>();

            try
            {
                con.Open();

                string sqlQuery = "SELECT id, name, type FROM [Algorithm]";
                SqlCommand command = new SqlCommand(sqlQuery, con);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int id = (int)reader["id"];
                    string name = reader["name"].ToString();
                    string type = reader["type"].ToString();

                    // add algorithm to dictionary
                    algorithms[id] = new Algorithm(id, name, type);
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return algorithms;
        }

        public static void AddAlgorithm(Algorithm algorithm)
        {
            try
            {
                con.Open();

                string commandString = "INSERT INTO [Algorithm] VALUES (\'" +
                    algorithm.Id + "\', \'" + algorithm.Name + "\', \'" +
                    algorithm.Type + "\')";

                SqlCommand command = new SqlCommand(commandString, con);
                command.ExecuteNonQuery();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }
        }

        public static Dictionary<int, Key> GetKeys()
        {
            Dictionary<int, Key> keys = new Dictionary<int, Key>();

            try
            {
                con.Open();

                string sqlQuery = "SELECT id, encrypt, decrypt FROM [Key]";
                SqlCommand command = new SqlCommand(sqlQuery, con);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int id = (int)reader["id"];
                    string encrypt = reader["encrypt"].ToString();
                    string decrypt = reader["decrypt"].ToString();

                    // add key to dictionary
                    keys[id] = new Key(id, encrypt, decrypt);


                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return keys;
        }

        public static void AddKey(Key key)
        {
            try
            {
                con.Open();

                string commandString = "INSERT INTO [KEY] VALUES (\'" +
                    key.Id + "\', \'" + key.EncryptKey + "\', \'" + key.DecryptKey + "\')";

                SqlCommand command = new SqlCommand(commandString, con);

                command.ExecuteNonQuery();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }
        }

        public static Dictionary<int, Performance> GetPerformances()
        {
            Dictionary<int, Performance> Performance = new Dictionary<int, Performance>();

            try
            {
                con.Open();

                string sqlQuery = "SELECT id, id_file, time_in_ms, id_frame, type FROM [Performance]";
                SqlCommand command = new SqlCommand(sqlQuery, con);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int id = (int)reader["id"];
                    int idFile = (int)reader["id_file"];
                    int timeInMs = (int)reader["time_in_ms"];
                    int idFrame = (int)reader["id_frame"];
                    string type = (string)reader["type"];

                    // add performance to dictionary
                    Performance[id] = new Performance(id, idFile, timeInMs, idFrame, type);
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return Performance;
        }

        public static List<string> getAllPerformancesNames()
        {
            Dictionary<int, Performance> Performances = GetPerformances();
            List<string> filesName = new List<string>();
            
            for (int i = 1; i <= Performances.Count; i++)
            {
                filesName.Add(Database.GetFile(Performances[i].IdFile));
            }

            return filesName;
        }
        public static void AddPerformance(Performance performance)
        {
            try
            {
                con.Open();

                string commandString = "INSERT INTO [Performance] VALUES (\'" + performance.Id + "\', \'" +
                    performance.IdFile + "\', \'" + performance.TimeInMs + "\', \'" +
                    performance.IdFramework + "\', \'" + performance.Type + "\')";

                SqlCommand command = new SqlCommand(commandString, con);
                command.ExecuteNonQuery();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }
        }

        public static string GetFrameworkName(int ID)
        {
            string framework = null;

            try
            {
                con.Open();

                string sqlQuery = "SELECT name FROM [Framework] WHERE id = @ID";
                SqlCommand command = new SqlCommand(sqlQuery, con);
                command.Parameters.AddWithValue("@ID", ID);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {

                    string name = reader["name"].ToString();

                    // add file to dictionary
                    framework = name;
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return framework;
        }
        public static int GetNewId(string tableName)
        {
            int id = 0;

            try
            {
                con.Open();

                string sqlQuery = "SELECT (count(*)+1) as \"index\" FROM [" + tableName +"]";
                SqlCommand command = new SqlCommand(sqlQuery, con);

                SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    id = (int)reader["index"];
                }
                reader.Close();

                con.Close();
            }
            catch (Exception ex)
            {
                //If an exception occurs, write it to the console
                Console.WriteLine(ex.ToString());
            }

            return id;
        }
    }
}
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Decodeur
{
    public partial class Encrypt_Decrypt : Form
    {
        public Encrypt_Decrypt()
        {
            InitializeComponent();

            radioButton1.CheckedChanged += new EventHandler(RadioButton_CheckedChanged);
            radioButton2.CheckedChanged += new EventHandler(RadioButton_CheckedChanged);

            radioButton1.Checked = true;
        }

        private void RadioButton_CheckedChanged(object sender, EventArgs e)
        {

            if (radioButton1.Checked)
            {
                
                comboBox1.Items.Clear();
                List<string> files = Database.getAllFiles(true);

                foreach (string file in files)
                {
                    comboBox1.Items.Add(file);
                }
            }
            else if (radioButton2.Checked)
            {
                
                comboBox1.Items.Clear();
                List<string> files = Database.getAllFiles(false);

                foreach (string file in files)
                {
                    comboBox1.Items.Add(file);
                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        // Proceed button
        private void button2_Click(object sender, EventArgs e)
        {
            string fileName = comboBox1.SelectedItem.ToString();

            List<File> files = Database.GetFiles().Values.ToList();

            File file = null;
            foreach (File f in files)
            {
                if (f.Name == fileName)
                {
                    file = f;
                    break;
                }
            }

            if (radioButton1.Checked)
            {
                long startTime = DateTimeOffset.Now.ToUnixTimeMilliseconds();

                // encrypt
                Codec.Encode(file);
                long stopTime = DateTimeOffset.Now.ToUnixTimeMilliseconds();

                int time = (int)(stopTime - startTime);
                Console.WriteLine(time);


                int newId = Database.GetNewId("performance");
                Database.AddPerformance(new Performance(newId, file.Id, time, 1, "encode"));
            }
            else if (radioButton2.Checked)
            {
                long startTime = DateTimeOffset.Now.ToUnixTimeMilliseconds();

                // decrypt
                Codec.Decode(file);
                long stopTime = DateTimeOffset.Now.ToUnixTimeMilliseconds();

                int time = (int)(stopTime - startTime);
                Console.WriteLine(time);


                int newId = Database.GetNewId("performance");
                Database.AddPerformance(new Performance(newId, file.Id, time, 1, "decode"));
            }
        }

        private void buttonKeyGen_Click(object sender, EventArgs e)
        {
            Key newKey = OpenSSL.GenerateKey("AES");
            Database.AddKey(newKey);
        }
    }
}

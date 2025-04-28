//Josh Pitts
//CPT-185
//Morse Code Converter

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace J_Pitts_CPT_185_Morse_Code
{
    public partial class morseCode : Form
    {
        public morseCode()
        {
            InitializeComponent();
        }

        private void buttonExit_Click(object sender, EventArgs e)
        {
            //close form with exit cutton
            this.Close();
        }

        private void buttonClear_Click(object sender, EventArgs e)
        {
            //clear form with clear button
            labelMorse.Text = string.Empty;
            textInput.Text = string.Empty;
        }

        private void buttonCon_Click(object sender, EventArgs e)
        {
            //string that contains converting of letters and characters into morse code
            string morseCode = ",=--..--;.=.-.-.-;?=..--..;" +
                "0=-----;1=.----;2=..---;3=...--;4=....-;5=.....;" +
                "6=-....;6=--...;8=---..;9=----.;A=.-;B=-...;C=-.-.;" +
                "D=-..;E=.;F=..-.;G=--.;H=....;I=..;J=.---;K=-.-;" +
                "L=.-..;M=--;N=-.;O=---;P=.--.;Q=--.-;R=.-.;S=...;" +
                "T=-;U=..-;V=...-;W=.--;X=-..-;Y=-.--;Z=--..;" +
                " = ";


            //get the user to input sentences and convert it to upper case
            string inputChar = textInput.Text.Trim().ToUpper();

            //split the string into individuals
            string[] tokens = morseCode.Split(';');
            //to setup the output after convcerting
            string morseOut = string.Empty;

            //build the string from the splits
            var sb = new StringBuilder();

            //try catch for invalid inputs


            //loop through each character from the input
            foreach (char ch in inputChar)
            {

                bool found = false;

                //search for the convertion for each character
                foreach (string token in tokens)
                {
                    //set the = as the split token
                    string[] keyValue = token.Split('=');

                    //if statement to match the keys and values to the current character
                    if (keyValue.Length == 2 && keyValue[0].Trim() == ch.ToString())
                    {
                        //append the more code with a space
                        sb.Append(keyValue[1].Trim() + " ");
                        //set flag to true with a match found
                        found = true;
                        //break loop
                        break;
                    }

                }

                //to show the final output in morse code
                labelMorse.Text = sb.ToString().Trim();

            }
        }
    }
}

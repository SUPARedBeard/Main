//Josh Pitts
//CPT 206
//Lab 3


using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace J_Pitts_Lab_3
{
    //info display form
    public partial class stateInfo : Form
    {
        public stateInfo(string stateDetails)
        {
            InitializeComponent();

            stateLabel.Text = stateDetails;
        }

        //exit display form
        private void buttonExit_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}

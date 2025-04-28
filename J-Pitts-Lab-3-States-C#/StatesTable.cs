//Josh Pitts
//CPT 206
//Lab 3


using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Windows.Forms.VisualStyles;

namespace J_Pitts_Lab_3
{
    public partial class statesLab3 : Form
    {
        public statesLab3()
        {
            InitializeComponent();
            
        }

        private void statesLab3_Load(object sender, EventArgs e)
        {
            //clear combo box to start
            comboBoxStates.Items.Clear();

            // TODO: This line of code loads data into the 'statesDatabaseDataSet.Table' table. You can move, or remove it, as needed.
            this.tableTableAdapter.Fill(this.statesDatabaseDataSet.Table);

            //combo box for selecting a state to get info
           
            comboBoxStates.DataSource = this.statesDatabaseDataSet.Table; 
            comboBoxStates.DisplayMember = "StateName";  
            comboBoxStates.ValueMember = "StateID";

            //to ensure combo box is empty on start
            comboBoxStates.SelectedIndex = -1;

            //combo box for sorting by column
            comboSort.Items.Add("StateName");
            comboSort.Items.Add("Population");
            comboSort.Items.Add("Capital");
            comboSort.Items.Add("StateFlower");
            comboSort.Items.Add("StateBird");
            comboSort.Items.Add("StateColors");
            comboSort.Items.Add("LargestCities");
            comboSort.Items.Add("MedianIncome");
            comboSort.Items.Add("ComputerJobPercent");

            


        }

        
        

        private void statesBindingNavigatorSaveItem_Click(object sender, EventArgs e)
        {
            //to save with the save button, having issues getting it to work though so i passed it to the update button
            this.Validate();
            this.tableBindingSource.EndEdit();
            this.tableTableAdapter.Update(this.statesDatabaseDataSet.Table);
            MessageBox.Show("Changes saved successfully!", "Save Successful", MessageBoxButtons.OK, MessageBoxIcon.Information);
            
        }

        //button to display state info from the combo box
        private void btnStateInfo_Click(object sender, EventArgs e)
        {
            //if statement to make sure a state is selected in the combo box
            if (comboBoxStates.SelectedIndex == -1)
            {
                MessageBox.Show("Please select a state from the ComboBox.", "Selection Required", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return; 
            }

            //to select and display based on the state chosen
            if (comboBoxStates.SelectedIndex != -1)
            {
                int selectedStateId = (int)comboBoxStates.SelectedValue;

                var selectedState = (from state in this.statesDatabaseDataSet.Table
                                     where state.StateId == selectedStateId
                                     select state).FirstOrDefault();

                //if statement to display all the state info
                if (selectedState != null)
                {
                    string stateDetails = $"State Name: {selectedState.StateName}\n" +
                                  $"Population: {selectedState.Population}\n" +
                                  $"Capital: {selectedState.Capital}\n" +
                                  $"State Flower: {selectedState.StateFlower}\n" +
                                  $"State Bird: {selectedState.StateBird}\n" +
                                  $"State Colors: {selectedState.StateColors}\n" +
                                  $"Largest Cities: {selectedState.LargestCities}\n" +
                                  $"Median Income: {selectedState.MedianIncome:C}\n" +
                                  $"Computer Job Percentage: {selectedState.ComputerJobPercent}%\n" +
                                  $"Flag Description: {selectedState.FlagDescription}";

                    stateInfo stateInfoForm = new stateInfo(stateDetails);

                    stateInfoForm.ShowDialog();
                }
                //failsafe just incase nothing is selected
                else
                {
                    MessageBox.Show("Please select a state from the ComboBox");
                }
            }
        }

        //to close the form
        private void buttonExit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void buttonSearch_Click(object sender, EventArgs e)
        {
            //to trim out unecesary spaces and make input lower case to match whats in the table
            string searchStateName = textBoxSearch.Text.Trim().ToLower();

            //to make sure a state is typed into the search bar
            if (string.IsNullOrEmpty(searchStateName))
            {
                MessageBox.Show("Please enter a state name to search for.", "Search Required", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return; 
            }

            //to get the state from the data set and set the name to lower case so its not case sensitive
            var result = (from state in this.statesDatabaseDataSet.Table
                          where state.StateName.ToLower().Contains(searchStateName) 
                          select state).FirstOrDefault();

            //if statement to select the correect state from the input and display the information
            if (result != null)
            {
                //create the state details string
                string stateDetails = $"State Name: {result.StateName}\n" +
                                      $"Population: {result.Population}\n" +
                                      $"Capital: {result.Capital}\n" +
                                      $"State Flower: {result.StateFlower}\n" +
                                      $"State Bird: {result.StateBird}\n" +
                                      $"State Colors: {result.StateColors}\n" +
                                      $"Largest Cities: {result.LargestCities}\n" +
                                      $"Median Income: {result.MedianIncome:C}\n" +
                                      $"Computer Job Percentage: {result.ComputerJobPercent}%\n" +
                                      $"Flag Description: {result.FlagDescription}";

                //create the stateInfo form and pass the state details to it
                stateInfo stateInfoForm = new stateInfo(stateDetails);
                stateInfoForm.ShowDialog();
            }
            else
            {
                //failsafe if state is mispelled or not in the list
                MessageBox.Show("No state found matching the search criteria.");
            }
        }

       
        //filter button to filter based on job percent higher than 4
        private void buttonFilter_Click(object sender, EventArgs e)
        {
            tableBindingSource.Filter = "ComputerJobPercent > 4";
        }

        //to sort by column selected in the sort combo box
        private void buttonSort_Click(object sender, EventArgs e)
        {
            //to make sure something is selected in the sort combo box
            if (comboSort.SelectedIndex == -1)
            {
                MessageBox.Show("Please select a column to sort by.");
                return;  // Exit the method early if no item is selected
            }

            //get selected column name
            string sortBy = comboSort.SelectedItem.ToString();  

            //use LINQ to sort based on the selected column
            var sortedStates = from state in statesDatabaseDataSet.Table
                               orderby GetSortValue(state, sortBy)  //sort by the selected column
                               select state;

            //to display in the datagrid when sorted
            dataGridView1.DataSource = sortedStates.ToList();  
        }

        //to get the sorted value
        private object GetSortValue(DataRow state, string sortBy)
        {
            //handle sorting for all the columns
            switch (sortBy)
            {
                case "StateName":
                    return state["StateName"];
                case "Population":
                    return state["Population"];
                case "Capital":
                    return state["Capital"];
                case "StateFlower":
                    return state["StateFlower"];
                case "StateBird":
                    return state["StateBird"];
                case "StateColors":
                    return state["StateColors"];
                case "LargestCities":
                    return state["LargestCities"];
                case "MedianIncome":
                    return state["MedianIncome"];
                case "ComputerJobPercent":
                    return state["ComputerJobPercent"];
                default:
                    return state["StateName"];   //default sorting by StateName
            }
        }

        //update button to save inputs in the data grid
        private void buttonUpdate_Click(object sender, EventArgs e)
        {
            this.Validate();
            this.tableBindingSource.EndEdit();  //end the edit operation
            this.tableTableAdapter.Update(this.statesDatabaseDataSet.Table);  //save the changes to the database

            //display a successful update
            MessageBox.Show("Changes saved successfully!", "Update Successful", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
             
    }
}

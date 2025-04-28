namespace J_Pitts_CPT_185_Morse_Code
{
    partial class morseCode
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.buttonCon = new System.Windows.Forms.Button();
            this.buttonExit = new System.Windows.Forms.Button();
            this.buttonClear = new System.Windows.Forms.Button();
            this.textInput = new System.Windows.Forms.TextBox();
            this.labelMorse = new System.Windows.Forms.Label();
            this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
            this.SuspendLayout();
            // 
            // buttonCon
            // 
            this.buttonCon.Location = new System.Drawing.Point(66, 51);
            this.buttonCon.Name = "buttonCon";
            this.buttonCon.Size = new System.Drawing.Size(98, 38);
            this.buttonCon.TabIndex = 1;
            this.buttonCon.Text = "&Convert";
            this.toolTip1.SetToolTip(this.buttonCon, "Convert to Morse Code");
            this.buttonCon.UseVisualStyleBackColor = true;
            this.buttonCon.Click += new System.EventHandler(this.buttonCon_Click);
            // 
            // buttonExit
            // 
            this.buttonExit.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.buttonExit.Location = new System.Drawing.Point(542, 532);
            this.buttonExit.Name = "buttonExit";
            this.buttonExit.Size = new System.Drawing.Size(98, 38);
            this.buttonExit.TabIndex = 1;
            this.buttonExit.Text = "E&xit";
            this.toolTip1.SetToolTip(this.buttonExit, "Exit");
            this.buttonExit.UseVisualStyleBackColor = true;
            this.buttonExit.Click += new System.EventHandler(this.buttonExit_Click);
            // 
            // buttonClear
            // 
            this.buttonClear.Location = new System.Drawing.Point(131, 532);
            this.buttonClear.Name = "buttonClear";
            this.buttonClear.Size = new System.Drawing.Size(98, 38);
            this.buttonClear.TabIndex = 2;
            this.buttonClear.Text = "C&lear";
            this.toolTip1.SetToolTip(this.buttonClear, "Clear Conversion");
            this.buttonClear.UseVisualStyleBackColor = true;
            this.buttonClear.Click += new System.EventHandler(this.buttonClear_Click);
            // 
            // textInput
            // 
            this.textInput.Location = new System.Drawing.Point(220, 57);
            this.textInput.Name = "textInput";
            this.textInput.Size = new System.Drawing.Size(373, 26);
            this.textInput.TabIndex = 0;
            this.toolTip1.SetToolTip(this.textInput, "Please input your sentence");
            // 
            // labelMorse
            // 
            this.labelMorse.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.labelMorse.Font = new System.Drawing.Font("Microsoft Sans Serif", 21.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.labelMorse.Location = new System.Drawing.Point(91, 162);
            this.labelMorse.Name = "labelMorse";
            this.labelMorse.Size = new System.Drawing.Size(531, 307);
            this.labelMorse.TabIndex = 4;
            this.labelMorse.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.toolTip1.SetToolTip(this.labelMorse, "Your sentence converted to Morse Code");
            // 
            // morseCode
            // 
            this.AcceptButton = this.buttonCon;
            this.AutoScaleDimensions = new System.Drawing.SizeF(10F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ActiveCaption;
            this.CancelButton = this.buttonExit;
            this.ClientSize = new System.Drawing.Size(732, 582);
            this.Controls.Add(this.labelMorse);
            this.Controls.Add(this.textInput);
            this.Controls.Add(this.buttonClear);
            this.Controls.Add(this.buttonExit);
            this.Controls.Add(this.buttonCon);
            this.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Margin = new System.Windows.Forms.Padding(5, 5, 5, 5);
            this.Name = "morseCode";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Morse Code Converter";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button buttonCon;
        private System.Windows.Forms.Button buttonExit;
        private System.Windows.Forms.Button buttonClear;
        private System.Windows.Forms.TextBox textInput;
        private System.Windows.Forms.Label labelMorse;
        private System.Windows.Forms.ToolTip toolTip1;
    }
}


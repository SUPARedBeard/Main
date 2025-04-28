CREATE TABLE [dbo].[Table]
(
	[StateId] INT NOT NULL PRIMARY KEY, 
    [StateName] NVARCHAR(50) NOT NULL, 
    [Population] BIGINT NOT NULL, 
    [FlagDescription] TEXT NOT NULL, 
    [StateFlower] NVARCHAR(50) NOT NULL, 
    [StateBird] NVARCHAR(50) NOT NULL, 
    [StateColors] NVARCHAR(50) NOT NULL, 
    [LargestCities] NVARCHAR(MAX) NOT NULL, 
    [Capital] NVARCHAR(50) NOT NULL, 
    [MedianIncome] DECIMAL(18, 2) NOT NULL, 
    [ComputerJobPercent] DECIMAL(5, 2) NOT NULL
)

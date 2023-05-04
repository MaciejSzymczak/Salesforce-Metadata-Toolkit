# Salesforce Metadata Toolkit

* LayoutAnalyser - produces a matrix that shows what fields are used on what page layouts (the matrix: fields x page layouts).
* WhoHasAccessToObject- produces a file showing what profiles can CRUD specific custom object
* UpdateProfiles - updates a list of Salesforce profiles: removes or grants access to certain fields.
* PageLayoutAssignment - exports the Salesforce matrix: page layout assignments
* SalesforceDependencies - perform full search of apex classes (you can provide the list of keywords like custom objects, profiles etc.)

@author Maciej Szymczak
@version 2023.05.04

# More about LayoutAnalyzer

This java program produces a matrix that shows what fields are used on what page layouts (the matrix: fields x page layouts).
It reads the content of page layouts and the definition of the object (download the .layout and .object files using migration tool) and conglomerates data in a form of a readable matrix. 
It Reads given folder on the disk (entry parameter of java program) and saves the result in the file result.csv.
It analyzes page layouts, not visual force pages (so far).
It can be used for identification what region uses what fields and for identification what fields are not used at all.
Column C says the number of page layouts on which field is used. 0 mean the field is suspicious and should be prosecuted  rather than instant death sentence. 

@param  Folder name with <pageLayuouts>.layout and <objectdefinition>.object
@return result.csv

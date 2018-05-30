# Sfdc.Metadata.LayoutAnalyzer

 * This java program  produces a matrix that shows what fields are used on what page layouts (the matrix: fields x page layouts).
 * It reads the content of page layouts and the definition of the object (you can download the .layout and .object files using migration tool) and conglomerates data in a form of a readable matrix. 
 * It Reads given folder on the disk (entry parameter of java program) and saves the result in the file result.csv.
 * It analyzes page layouts, not visual force pages (so far).
 * It can be used for identification what region uses what fields and for identification what fields are not used at all.
 * Column C says the number of page layouts on which field is used. 0 mean the field is suspicious and should be prosecuted  rather than instant death sentence. 
 * 
 * @author Maciej Szymczak
 * @version 2016.11.23
 * @param  Folder name with <pageLayuouts>.layout and <objectdefinition>.object
 * @return result.csv

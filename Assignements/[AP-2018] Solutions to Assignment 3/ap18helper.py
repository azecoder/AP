from os import listdir, path, rename, walk, mkdir
from subprocess import call
import sys, urllib.request, csv
import logging

# const variables
URL = "http://pages.di.unipi.it/corradini/Didattica/AP-18/PROG-ASS/03/Test/"

EXT_JAR = ".jar"
EXT_RAJ = ".raj"
EXT_LAN = [".py", ".hs", ".java"]
EXT_JAVA = ".java"
# 

logging.basicConfig(level=logging.INFO)

# Exercise 1 -----------------------------------------------------------------------------------------------
def convert_extensions(root):
    for file_name in listdir(root):                 # we look all files in the folder
        cur_file = path.join(root, file_name)
        if not path.isfile(cur_file):               # if it is a folder
            convert_extensions(cur_file)            # we will search for .raj files in this folder
        base_file, ext = path.splitext(file_name)   # we get name and extension of the file
        if ext == EXT_RAJ:                          # if extension is equal to .raj 
            new_name = cur_file.replace(EXT_RAJ, EXT_JAR)   # we set a new name with jar extension
            rename(cur_file, new_name)                      # and we rename it
            logging.info(15 * '-' + " File (" + file_name + "), extension changed..\n")
            logging.info(15 * '-' + " New Path : " + new_name + " \n\n")
def raj2jar(root):
    logging.info("\n" + 30 * '=' + " CONVERT EXTENSIONS " + 30 * '=' + "\n")
    convert_extensions(root)    # find files in folders recursively, and change extensions
# Exercise 1 - FINISH --------------------------------------------------------------------------------------

# Exercise 2 -----------------------------------------------------------------------------------------------
def get_paths(root, sources, ind, path_list):
    for file_name in listdir(root):                 # we look all files in the folder
        cur_file = path.join(root, file_name)   
        if not path.isfile(cur_file):                       # if it is a folder
            get_paths(cur_file, sources, ind, path_list)    # start searching in this folder
        base_file, ext = path.splitext(file_name)   # get extension of the file
        if ext in EXT_LAN:                          # if it is the one of the searched extensions
            path_list.append(cur_file[ind:])        # add this file path to the list

def collect_sources(root, sources):
    logging.info("\n" + 30 * '=' + " COLLECT SOURCES " + 30 * '=' + "\n")
    path_list = []
    get_paths(root, sources, len(root), path_list)  # get paths as a list
    source_file = open(root + sources, "w+")    # create a source file
    for path in path_list:                      # for each path in the path list
        source_file.write(path + "\n")          # write path into the source file
    source_file.close()                         # close the source file
    logging.info(15 * '-' + " File ( " + sources + " ) created..\n")
    logging.info(15 * '-' + " Paths inserted to File ( " + sources + " ) ..\n")
# Exercise 2 - FINISH --------------------------------------------------------------------------------------

# Exercise 3 -----------------------------------------------------------------------------------------------
def get_java_paths(root, path_list):
    for file_name in listdir(root):     # we look all files in the folder
        cur_file = path.join(root, file_name)
        if not path.isfile(cur_file):   # if it is a folder
            get_java_paths(cur_file, path_list) # start searching in this folder
        base_file, ext = path.splitext(file_name)   # get extension of the file
        if(ext == EXT_JAVA):            # if it is java extension
            path_list.append(cur_file)  # add this to the list

def get_package_name(java_path):
# in this part, I check comments in java file.
# if there is any package name in commmented area,
# then i will not get this package name, else i will
# so i check for "//" and "/* */" comments in java files
    have_comment = 0
    java_file = open(java_path, "r", encoding = "ISO-8859-1")    # open java file
    for row in java_file:              # check each line
        # if commented with //
        xline = row.replace(' ', '')   # remove all spaces from line
        if(xline[:2] == "//"):          # if we have a line with started "//" comment
            continue                    # then i skip this line
        # if commented with /* */
        # if we have "/*" in line, then we will check for "*/" in this and next lines
        if have_comment == 0:               # it means there is not any commented area started with "/*"
            start_ind = row.find("/*")     # so, we check for "/*" in each line
            if(start_ind != -1):            # if we have
                have_comment = 1            # we find a starting point of the comment, so we set status true.
            nline = row[(start_ind+1):]    # firstly, we are looking for "*/" in this line
            end_ind = nline.find("*/")      # we check for "*/" in current line
            if(end_ind != -1):              # if there is, then we will skip this line
                have_comment = 0            # and set status false 
        else:
            end_ind = row.find("*/")       # we are looking for "*/" in other lines
            if(end_ind != -1):              # if there is, then we will skip all commented lines
                have_comment = 0            # and set status false 
        if(have_comment == 1):              # in each commented line
            continue                        # we will skip
        # find package name in non commented line
        ind = xline.find("package")         # else we will check for "package"
        if ind != -1:                       # if there is
            x = xline[(ind + 7):]           # i will remove word "package"
            x = x[:(x.find(";"))]           # i will get word before ";"
            return x                        # return the package name 
    return None

def get_file_name(java_path):
    file_name = java_path                   # for ex: a/b/c/d/hw.java
    while(file_name.find("/") != -1):       # i search for "/" in line, if there is not it means i find the file name
        idx = file_name.find("/") + 1       # else i will remove first part and check again
        file_name = file_name[idx:]         # "a/b/c/d/hw.java" will change to "b/c/d/hw.java"
    
    return file_name                        # return the name of the file from the given path

def create_move(root, package, java_path):
    if(package == "" or package == None):           # at the end of the recursive function, u will not have any package name
        file_name = get_file_name(java_path)        # get name of the java file from the path
        if java_path != root + file_name:           # if path of the java file is different than current path
            rename(java_path, root + file_name)     # then move it
            logging.info(15 * '-' + " File ( " + file_name + " ) moved..\n")
        return

    # package name. for ex : winners.general
    # it means that, i will move java file to /path/winners/general
    dot_index = package.find(".")       # find string before "."
    folder_name = package[:dot_index]   # it is name of the folder
    curr = path.join(root, folder_name) 
    if not path.isdir(curr):            # if there is not any folder with name "folder_name"
        mkdir(curr)                     # create it
        logging.info(15 * '-' + " Folder ( " + folder_name + " ) created..\n")

    create_move(curr + "/", package[(dot_index+1):], java_path) # go to this folder, and do it again. recursively

def rebuild_packages(root):
    logging.info("\n" + 30 * '=' + " REBUILD PACKAGES " + 30 * '=' + "\n")
    path_list = []          # create a new empty path list for paths
    get_java_paths(root, path_list)     # get all java paths using recursive function
    for path in path_list:              # and check all path in this path list
        package_name = get_package_name(path)   # get package name from each java file
        if(package_name):                       # if there is any package name
            create_move(root, package_name + ".", path) # create folder and move this file 
# Exercise 3 - FINISH -------------------logging.basicConfig(level=logging.INFO)-------------------------------------------------------------------


# Exercise 4 -----------------------------------------------------------------------------------------------
def exist_file(fname, root):
    for file_name in listdir(root):             # check each file in folder
        cur_file = path.join(root, file_name)   
        if (path.isfile(cur_file) and file_name == fname):  # if there is a file with name "fname"
            return 1                                        # it means, file exists..
    return 0                                                # else not

def find_file_and_test(root, file_name, test_file_name, run_command):
    for fname in listdir(root):             # for each file in root folder
        cfile = path.join(root, fname)      
        if not path.isfile(cfile):          # if it is a folder
            find_file_and_test(cfile, file_name, test_file_name, run_command) # go this folder, and check inside of this folder
        elif(fname == file_name):           # if it is a file, and name is equal to given file name
            logging.info(15 * '=' + " Testing File ( " + file_name + " ) " + 15 * '=' + "\n")
            for tfile in test_file_name:    # get each test file from "test file list"
                if(tfile == ""):            
                    logging.warning(15 * '-' + " There is not any test file..\n")          
                else:                       # if there is a test file
                    if(exist_file(tfile, root)):    # check if test file exists or not
                        logging.warning(15 * '-' + " Testing File ( " + tfile + " ) exists..\n")
                    else:                           # if not exists
                        urllib.request.urlretrieve(URL + tfile, root + "/" + tfile)  # then download this test file
                        logging.info(15 * '-' + " Testing File ( " + tfile + " ) downloaded from URL\n")
            logging.info("\n" + 15 * '*' + " Run Command for File( " + file_name + " ) " + 15 * '*' + "\n")  
            try:
                call(run_command, shell=True, cwd=root) # after download test files, run given command
                logging.info("" + 15 * '-' + " COMMAND RUNS SUCCESSFULLY " + 15 * '-' + "\n")
            except:
                logging.error( + 15 * '-' + " SOMETHING WENT WRONG! " + 15 * '-' + "\n")


def download_tests(root):
    csv_file_name = "AP_TestRegistry.csv"
    logging.info("\n" + 30 * '=' + " DOWNLOAD TESTS " + 30 * '=' + "\n")
    try:
        urllib.request.urlretrieve(URL + csv_file_name, csv_file_name)      # download file from URL
        logging.info(15 * '-' + " File ( " + csv_file_name + " ) downloaded from URL..\n")
    except:
        logging.error(15 * '-' + " DOWNLOAD ERROR! \n")
    
    logging.info("\n" + 30 * '=' + " Start Testing " + 30 * '=' + "\n")
    with open(csv_file_name) as csv_file:       # open the file
        csv_reader = csv.reader(csv_file)       # read data
        line = 0                                # and set a line for first line (name of the columns)
        for row in csv_reader:                  # for each row in file
            if line > 0:                        # if line index is equal to zero, we will skip this line
                _fname = str(row[0]).strip()                        # get file name
                _tname = str(row[1]).strip().split(":")             # get test file names
                _command = str(row[2]).strip().replace("\"", '')    # get run command
                find_file_and_test(root, _fname, _tname, _command)  # call a function to download test files, and run commands
            line = line + 1                     # increase line index
# Exercise 4 - FINISH --------------------------------------------------------------------------------------


# Exercise 5 -----------------------------------------------------------------------------------------------
class Operation:
    def __init__(self, index, operation_name, function_name):
        self.index = index
        self.operation_name = operation_name
        self.function_name = function_name

def my_help():
    print("  help  |\tHelp & Show operation table")
    print("--------|----------------------------------------------------------------------------")
    print("  exit  |\tExit program")
    print("--------|----------------------------------------------------------------------------\n")

def show(operations):
    print("\n\nChoose the operation Number from given table\n")
    print("    N   |\tFUNCTIONS")
    print("--------|----------------------------------------------------------------------------")
    for op in operations:
        print("    " + str(op.index) +  "   |\t" + op.operation_name)
        print("--------|----------------------------------------------------------------------------")
    print("\n")

def main():
    print("\n--------------------------------------------------------------------------------------")
    print("\tBefore the operation, you need enter ROOT path and source file name")
    print("--------------------------------------------------------------------------------------\n")

    input_PATH = input("\t1. Please enter the path as a root: ")                # /home/azecoder/Desktop/AP/HW-python/
    input_SOURCE = input("\t2. Please enter the name of the sources file: ")    # src.txt

    # if(str(input_PATH) == ""):
    #     input_PATH = "/home/azecoder/Desktop/AP/HW-python/"
    # if(str(input_SOURCE) == ""):
    #     input_SOURCE = "src.txt"

    operations = [
        Operation(1, "Convert raj files to jar files", lambda: raj2jar(input_PATH)),
        Operation(2, "Collect paths of the files with extension java, py, hs", lambda: collect_sources(input_PATH, input_SOURCE)),
        Operation(3, "Rebuild the packages", lambda: rebuild_packages(input_PATH)),
        Operation(4, "Retrieving the files for testing", lambda: download_tests(input_PATH))
    ]

    show(operations)

    while True:
        input_N = input("\tPlease enter op/number: ")

        if(input_N.lower() == "help"):
            show(operations)
            my_help()
        elif(input_N.lower() == "exit"):
            print("\nProgram successfully finished.. bye\n")
            sys.exit()
        else:
            print("\n\n")
            # print("-------------------------------------------------------------------------------------")
            # print("\t\t" + operations[int(input_N) - 1].operation_name)
            # print("-------------------------------------------------------------------------------------\n")
            
            operations[int(input_N) - 1].function_name()

        print("\n")

# Exercise 5 - FINISH --------------------------------------------------------------------------------------

# call main function 
main()
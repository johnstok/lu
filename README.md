# lu

`lu` is a command line tool for listing URL shortcuts. It can parse the following formats:

* webloc (OSX)
* url (Linux)
* desktop (Windows)

## Usage

Running `lu` for a file will extract the URL:

    user$ lu example.webloc
    http://example.com/
    
Running `lu` for a folder will process all contents of the folder:

    user$ lu shortcuts/
    http://foo.com/
    http://bar.com/
    
Passing the `-r` switch will cause `lu` to recurse into sub-folders.
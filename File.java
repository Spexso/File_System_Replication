public class File extends FileSystemElement {
    
    /**
     * File Constructor
     * @param name represents the name of file
     * @param parent represents the parent of file
     */
    public File(String name, FileSystemElement parent){

        super(name, parent);
    }

    /**
     * Overridden print method to print name of file with "" character
     */
    @Override
    public void print(String prefix){
        
        System.out.println(prefix + GetName());
    }
}

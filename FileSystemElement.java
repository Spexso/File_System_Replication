import java.sql.Timestamp;

public abstract class FileSystemElement {

    // Name of element
    protected String name;
    
    // Timestamp information about creation time
    protected Timestamp dateCreated;

    // Reference to the parent directory
    // Null means its root
    protected FileSystemElement parent;

    public FileSystemElement(String name, FileSystemElement parent){
        
        this.name = name;
        this.parent = parent;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }

    /**
     * 
     * @return
     */
    public String GetName(){
        return this.name;
    }

    /**
     * 
     * @return
     */
    public FileSystemElement GetParent(){
        return this.parent;
    }

    /**
     * 
     * @return
     */
    public Timestamp GetDateCreated(){
        return this.dateCreated;
    }

    /**
     * 
     * @param NewParent
     */
    public void SetParent(FileSystemElement NewParent){
        this.parent = NewParent;
    }

    /**
     * 
     * @param prefix
     */
    public abstract void print(String prefix);
}

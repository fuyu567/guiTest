import java.io.File;
import java.util.LinkedList;




public class JavaTest {

	static final String REGULAR_M="/extdata/adas/DriveRecord";
	static final String EVENT_M = "/extdata/adas/EventRecord";
	
	
	static final String PATH1="/extdata/adas/DriveRecord/2016_09";
	static final String PATH2="/extdata/adas/DriveRecord/2016_09/09_12";
	static final String PATH3="/extdata/adas/DriveRecord/2016_09/09_26";
	static final String PATH4="/extdata/adas/DriveRecord/2016_09/09_12/2016_0912_0028_00_02_RM.avi";
	static final String PATH5="/extdata/adas/DriveRecord/2016_09/09_26/2016_0926_0028_00_00_RM.avi";
	static final String PATH6="/extdata/adas/DriveRecord/2016_09/09_12/2016_0912_0028_00_01_RM.avi";
	static final String PATH7="/extdata/adas/DriveRecord/2016_09/09_26/2016_0926_0028_00_01_RM.avi";
	
	static ADASListFile root=new ADASListFile(REGULAR_M, "0", 0, true);
	
	
	
	public static void main(String[] args) {
		addListFile(getParentFile(getFolderName(PATH1)), getFolderName(PATH1));
		System.out.println("----------------------");
		addListFile(getParentFile(getFolderName(PATH2)), getFolderName(PATH2));
		System.out.println("----------------------");
		addListFile(getParentFile(getFolderName(PATH3)), getFolderName(PATH3));
		System.out.println("----------------------");
		addListFile(getParentFile(getFileName(PATH4)), getFileName(PATH4));
		System.out.println("----------------------");
		addListFile(getParentFile(getFileName(PATH5)), getFileName(PATH5));
		System.out.println("----------------------");
		addListFile(getParentFile(getFileName(PATH6)), getFileName(PATH6));
		System.out.println("----------------------");
		addListFile(getParentFile(getFileName(PATH7)), getFileName(PATH7));
	}
	
	static ADASListFile getParentFile(ADASListFile tempAdasListFile){
		ADASListFile adasListFile=root;
		String tempString=tempAdasListFile.getPath().replace(REGULAR_M, "");
		tempString=tempString.startsWith(File.separator)?tempString.substring(1, tempString.length()):tempString;
		tempString=tempString.endsWith(File.separator)?tempString.substring(0, tempString.length()-1):tempString;
		int N=getNum(tempString);
		for(int i=0;i<N-1;i++){
			for (ADASListFile adasListFile2: adasListFile.getChildren()) {
				if(adasListFile2.isAnchorOf(tempAdasListFile)){
					adasListFile=adasListFile2;
					break;
				}
			}
		}
		return adasListFile;
	}
	
	static void addListFile(ADASListFile parentFile,ADASListFile tempAdasListFile){
		for(int i=parentFile.getCheckPosition();i<=parentFile.getChildren().size();i++){
			if(i==parentFile.getChildren().size()){
				parentFile.getChildren().add(i, tempAdasListFile);
				parentFile.setCheckPosition(++i);
				break;
			}
		}
	}
	
	
	static int getNum(String path){
		int num=0;
		num=path.split(File.separator).length;
		return num;
	}
	
	static ADASListFile getFileName(String path){
		String [] ss=path.split(File.separator);
		String name=ss[ss.length-1];
		ADASListFile adasListFile=new ADASListFile(path, name, 0, false);
		return adasListFile;
	}
	
	static ADASListFile getFolderName(String path){
		String [] ss=path.split(File.separator);
		String name=ss[ss.length-1];
		ADASListFile adasListFile=new ADASListFile(path, name, 0, true);
		return adasListFile;
	}
	
}


class ADASListFile{
	
	private String path = "";
	private String name = "";
	private long modifyTime = -1L;
	private boolean isFolder = false;
	/** 以下为文件夹所使用 */
	private volatile  int checkPosition = 0; // checked position
	private volatile int descendantCount = 0; // 子孙个数
	private LinkedList<ADASListFile> children = new LinkedList<ADASListFile>(); // folder case
	
	
	public ADASListFile(String path, String name, long modifyTime,
			boolean isFolder) {
		this.path = path;
		this.name = name;
		this.modifyTime = modifyTime;
		this.isFolder = isFolder;
	}
	public long getModifyTime(){
		return this.modifyTime;
	}
	public void setModifyTime(long time){
		this.modifyTime = time;
	}
	public String getPath(){
		return this.path;
	}
	public boolean isFolder(){
		return this.isFolder;
	}
	public LinkedList<ADASListFile> getChildren() {
		return children;
	}
	public void setChildren(LinkedList<ADASListFile> children) {
		this.children = children;
	}
	
	public int getCheckPosition() {
		return checkPosition;
	}
	public void setCheckPosition(int checkPosition) {
		this.checkPosition = checkPosition;
	}
	
	public String getName() {
		return name;
	}
	public int getDescendantCount() {
		return descendantCount;
	}
	public void setDescendantCount(int descendantCount) {
		this.descendantCount = descendantCount;
	}
	
	
	public boolean isAnchorOf(ADASListFile file){
		if(file == null){
			return false;
		}
		if(!this.isFolder){
			return false;
		}
		String p1 = this.path.endsWith(File.separator)?this.path:this.path + File.separator;
		String p2 = file.getPath().endsWith(File.separator)?file.getPath().substring(0, file.getPath().length()-1):file.getPath();
		
		if(!p2.startsWith(p1)){
			return false;
		}
		return true;
	}
}

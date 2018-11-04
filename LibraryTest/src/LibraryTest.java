import java.io.*;
import java.util.*;
import java.lang.String;

public class LibraryTest {

    static final String FilePath = "E:\\my file\\study\\编程\\JavaBase-master\\LibraryTest\\src\\library.bib";

    public static void main(String[] args) {

        Library library = new Library();
        Scanner input =new Scanner(System.in);
        System.out.println("请输入你要选择的功能：\n1、查找文献\n2、判断两名作者是否写过同一本书\n3、为所有文献排序\n4、退出");
        while (input.hasNextLine()) {
            switch (input.next()) {
                case "1":
                    System.out.println("请输入你要查找的文献的名字和作者：");
                    input.nextLine();
                    Publication publication = null;
                    publication = library.FindPubByTitle(input.nextLine());
                    if (publication == null)
                        System.out.println("对不起没有查到！");
                    else System.out.println(publication.title+"\t"+publication.authors);
                    System.out.println("您可以继续输入要选择的功能：\n1、查找文献\n2、判断两名作者是否写过同一本书\n3、为所有文献排序\n4、退出");
                    break;
                case "2":
                    System.out.println("请输入2个作者的名字：");
                    input.nextLine();
                    String name1 = input.nextLine();
                    System.out.println(library.IsWriteTogether(name1, input.nextLine()));
                    System.out.println("您可以继续输入要选择的功能：\n1、查找文献\n2、判断两名作者是否写过同一本书\n3、为所有文献排序\n4、退出");
                    break;
                case "3":
                    library.SortPublication();
                    System.out.println("您可以继续输入要选择的功能：\n1、查找文献\n2、判断两名作者是否写过同一本书\n3、为所有文献排序\n4、退出");
                    break;
                case "4":
                    System.exit(0);
                default:
                    System.out.println("输入错误,请重新输入：");
            }
        }
        input.close();
    }
}
/*
 * 定义一个文献类
 *它的子类有两个
 * */

abstract class Publication implements Comparable {
    protected String title, pages;
    protected int year;
    protected ArrayList<String> authors;

    public int compareTo(Publication publication) {
        if (this.year > publication.year)
            return 1;
        else if (this.year < publication.year)
            return -1;
        else return this.authors.get(0).compareTo(publication.authors.get(0));
    }
}

/*
 * 定义了文献库类
 * 包含了文献
 * @author 朱孝曦
 * */
class Library {
    ArrayList<Publication> publications;

    Library() {
        this.publications = new ArrayList<Publication>();
        this.ReadLibrary();
    }


    /*
     * 注意只读取@article和@inproceedings
     * */
    void ReadLibrary() {//读取bib文件并且初始化Library以及初始化publication
        File file = new File(LibraryTest.FilePath);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String content;
        int articleIndex, inproceedingsIndex, index, booktitleIndex, titleIndex, authorIndex, journalIndex, pagesIndex, yearIndex, numberIndex, startIndex, endIndex;
        try {
            String temp = new String();
            int year, number;
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            char[] cbuf = new char[50000];
            bufferedReader.read(cbuf);
            content = new String(cbuf);
            articleIndex = content.indexOf("article");
            inproceedingsIndex = content.indexOf("inproceedings");
            do {

                titleIndex = content.indexOf("title", articleIndex);
                startIndex = content.indexOf('{', titleIndex);
                endIndex = content.indexOf('}', titleIndex);
                String title = content.substring(startIndex + 1, endIndex);

                authorIndex = content.indexOf("author", articleIndex);
                startIndex = content.indexOf('{', authorIndex);
                index = endIndex = content.indexOf('}', authorIndex);
                String author = content.substring(startIndex + 1, endIndex);

                ArrayList<String> authorList = new ArrayList<>();
                int count = -1;
                do {

                    endIndex = content.indexOf(',', startIndex+1);
                    authorList.add(content.substring(startIndex + 1, endIndex).trim());
                    startIndex = endIndex;
                    count++;
                }while (endIndex<index);
                authorList.set(count,authorList.get(count).substring(0,authorList.get(count).length()-1));//已经修改过了，但是另一个还待修改

                 journalIndex = content.indexOf("journal", articleIndex);
                startIndex = content.indexOf('{', journalIndex);
                endIndex = content.indexOf('}', journalIndex);
                String journal = content.substring(startIndex + 1, endIndex);

                pagesIndex = content.indexOf("pages", articleIndex);
                startIndex = content.indexOf('{', pagesIndex);
                endIndex = content.indexOf('}', pagesIndex);
                String pages = content.substring(startIndex + 1, endIndex);

                yearIndex = content.indexOf("year", articleIndex);
                startIndex = content.indexOf('{', yearIndex);
                endIndex = content.indexOf('}', yearIndex);
                temp = content.substring(startIndex + 1, endIndex);
                year = Integer.parseInt(temp);

                numberIndex = content.indexOf("number", articleIndex);
                startIndex = content.indexOf('{', numberIndex);
                endIndex = content.indexOf('}', numberIndex);
                temp = content.substring(startIndex + 1, endIndex);
                number = Integer.parseInt(temp);

                Publication tempPub = new Article(title, authorList, journal, pages, year, number);
                this.publications.add(tempPub);

                /*
                 * 此处小心，article往后面就没有了，所以会返回-1，所以又小于23393，如此陷入死循环
                 * */
                if (articleIndex != content.lastIndexOf("article"))
                    articleIndex = content.indexOf("article", articleIndex + 1);
                else articleIndex = 23393;
            } while (articleIndex < 23393);

            do {

                titleIndex = content.indexOf("title", inproceedingsIndex);
                startIndex = content.indexOf('{', titleIndex);
                endIndex = content.indexOf('}', titleIndex);
                String title = content.substring(startIndex + 1, endIndex);

                authorIndex = content.indexOf("author", inproceedingsIndex);
                startIndex = content.indexOf('{', authorIndex);
                index = endIndex = content.indexOf('}', authorIndex);
                String author = content.substring(startIndex + 1, endIndex);

                ArrayList<String> authorList = new ArrayList<>();
                int count = -1;
                do {

                    endIndex = content.indexOf(',', startIndex+1);
                    authorList.add(content.substring(startIndex + 1, endIndex).trim());
                    startIndex = endIndex;
                    count++;
                }while (endIndex<index);
                authorList.set(count,authorList.get(count).substring(0,authorList.get(count).length()-1));//已经修改过了，但是另一个还待修改

                booktitleIndex = content.indexOf("booktitle", inproceedingsIndex);
                startIndex = content.indexOf('{', booktitleIndex);
                endIndex = content.indexOf('}', booktitleIndex);
                String booktitle = content.substring(startIndex + 1, endIndex);

                pagesIndex = content.indexOf("pages", inproceedingsIndex);
                startIndex = content.indexOf('{', pagesIndex);
                endIndex = content.indexOf('}', pagesIndex);
                String pages = content.substring(startIndex + 1, endIndex);

                yearIndex = content.indexOf("year", inproceedingsIndex);
                startIndex = content.indexOf('{', yearIndex);
                endIndex = content.indexOf('}', yearIndex);
                temp = content.substring(startIndex + 1, endIndex);
                year = Integer.parseInt(temp);

                Publication tempPub = new Inproceedings(title, authorList, booktitle, pages, year);
                this.publications.add(tempPub);

                /*
                 * 此处小心，article往后面就没有了，所以会返回-1，所以又小于23393，如此陷入死循环
                 * */
                if (inproceedingsIndex != content.lastIndexOf("inproceedings"))
                    inproceedingsIndex = content.indexOf("inproceedings", inproceedingsIndex + 1);
                else inproceedingsIndex = 22478;
            } while (inproceedingsIndex < 22478);


        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.getStackTrace();
            }

        }
    }

    public Publication FindPubByTitle(String findTitle) {//通过名字来查找文件，或许还要写通过作者来查找名字

        for (Publication p:publications
             ) {
            if (p.title.equals(findTitle))
                return p;
        }
        for (Publication p:publications
             ) {
            for (String str:p.authors
                 ) {
                if (str.equals(findTitle))
                    return p;
            }
        }
        return null;
    }

    public boolean IsWriteTogether(String name1, String name2) {//检查两人是否写同一个文献要用comparable
        for (Publication p:publications
             ) {
            for (String str:p.authors
                 ) {
                if (str.equals(name1)){
                    for (String string:p.authors
                         ) {
                        if (string.equals(name2))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public void SortPublication() {//为文献排序

        Collections.sort(publications);
        for (Publication p:publications
             ) {
            System.out.println("title:\t"+p.title+"\nauthors:\t"+p.authors+"\nyear:\t"+p.year+"\npages:\t"+p.pages+"\n");
        }


    }
}

/*
 * 创建子类
 * */
class Article extends Publication {
    int number;
    String journal;

    Article(String title, ArrayList<String> authors, String journal, String pages, int year, int number) {
        super.title = title;
        super.authors = authors;
        super.pages = pages;
        super.year = year;
        this.journal = journal;
        this.number = number;

    }

    @Override
    public int compareTo(Object o) {
        Publication publication = (Publication) o;
        if (this.year > publication.year)
            return 1;
        else if (this.year < publication.year)
            return -1;
        else return this.authors.get(0).compareTo(publication.authors.get(0));

    }

}

class Inproceedings extends Publication {
    String booktitle;

    Inproceedings(String title, ArrayList<String> authors, String booktitle, String pages, int year) {
        super.title = title;
        super.authors = authors;
        super.pages = pages;
        super.year = year;
        this.booktitle = booktitle;
    }

    @Override
    public int compareTo(Object o) {
        Publication publication = (Publication) o;
        if (this.year > publication.year)
            return 1;
        else if (this.year < publication.year)
            return -1;
        else return this.authors.get(0).compareTo(publication.authors.get(0));

    }

}

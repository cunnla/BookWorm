package cunnla.cunnla.bookworm;

public class Book {

    String bookDate, bookName, bookAuthor, bookGenre, bookNotes, id;


     public Book(String bookDate, String bookName, String bookAuthor, String bookGenre, String bookNotes, String id){

       this.bookDate = bookDate;
       this.bookName = bookName;
       this.bookAuthor = bookAuthor;
       this.bookGenre = bookGenre;
       this.bookNotes = bookNotes;
       this.id = id;
     }

    @Override
    public String toString() {
        return this.bookDate + ": " + this.bookName + " by " + this.bookAuthor + ". "+ this.bookGenre;
    }
}

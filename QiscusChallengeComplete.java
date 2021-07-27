public class BookLibraryApplication
{
    public static void main(String[] args) {
    	// The following story is totally fiction. Any resemblances with real world character is (perhaps)
        // coincidence.
    	
        /*
        Bill Gates, the ambitious book collector.
        -----------------------------------------
        */

        /*
        Many people knows him as the founder and ex-CEO of one of the largest software company in the world,
        but not many knows that he's also an avid book collector.
        
        In fact, in one interview, he said that the reason he build his own company is that so he can build
        his own book library.
        */

        /*
        Now the time has come, he became one of the richest man in the planet. "I've been waiting for this
        for a really long, long time".
        Then he start building his own fiction book library.
        */
        FictionBookLibrary fictionBookLibrary = new FictionBookLibrary();

        /*
        He then go round the world. People know him go around the world because he's trying to cure malaria,
        to do philanthropic things, and whatnot. But little did they know that Bill is also collecting fiction
        books for his library.
        Travelling around the world, he found the rarest kind of fiction books he wants to collect, like 'Harry Potter'
        series by 'J.K. Rowling', 'The Lord of the Rings' series by 'J.R.R Tolkien', and 'Sherlock Holmes' series by
        'Arthur Conan Doyle'.
        */
        Book harryPotterAndTheSorcerersStone = new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling");
        Book harryPotterAndChamberOfSecrets = new Book("Harry Potter and the Chamber of Secrets", "J.K. Rowling");
        Book theFellowshipOfTheRing = new Book("The Fellowship of the Ring", "J.R.R. Tolkien");
        Book theTwoTowers = new Book("The Two Towers", "J.R.R. Tolkien");
        Book theAdventuresOfSherlockHolmes = new Book("The Adventures of Sherlock Holmes", "Arthur Conan Doyle");
        Book aStudyInScarlet = new Book("A Study in Scarlet", "Arthur Conan Doyle");

        /*
        Not wanting to waste his time, Bill bought several of those rare books. (Have I tell you that he has a lot of
        money? I mean, a really really lot of money?)
        
        The 'Harry Potter' series, for example, he bought 5 of those (2 books for 'Harry Potter and the Sorcerer's
        Stone', and 3 for 'Harry Potter and Chamber of Secrets'). 'The Lord of the Rings' series, on the other hand,
        proven to be quite a challenge even for Bill, he only got 3 of those (1 for 'The Fellowship of the Rings' and
        2 for 'The Two Towers'). Luckily, for 'Sherlock Holmes' series, he found and bought many of those (20 and 10
        books for 'The Adventures of Sherlock Holmes' and 'A Study in Scarlet', respectively.)
        */
        fictionBookLibrary.addCollection(harryPotterAndTheSorcerersStone, 2);
        fictionBookLibrary.addCollection(harryPotterAndChamberOfSecrets, 3);
        fictionBookLibrary.addCollection(theFellowshipOfTheRing, 1);
        fictionBookLibrary.addCollection(theTwoTowers, 2);
        fictionBookLibrary.addCollection(theAdventuresOfSherlockHolmes, 20);
        fictionBookLibrary.addCollection(aStudyInScarlet, 10);

        /*
        Bill really loves his book collection, he always list its collection every day.
        */
        java.util.List<Book> availableBooks = fictionBookLibrary.listAvailableBooks();

        System.out.println("1  : " + availableBooks.contains(harryPotterAndChamberOfSecrets));
        System.out.println("2  : " + availableBooks.contains(theFellowshipOfTheRing));
        System.out.println("3  : " + availableBooks.contains(theAdventuresOfSherlockHolmes));

        /*
        Of all the fiction writer that he loves, he adores 'Arthur Conan Doyle' the most. In fact, when he couldn't
        sleep in the middle of night, he often goes to his library just to check the books that authored by him. He
        said this gives him peace required for him to sleep.
        */
        java.util.List<Book> booksByArthurConanDoyle = fictionBookLibrary.listBooksAuthoredBy("Arthur Conan Doyle");
        
        System.out.println("4  : " + booksByArthurConanDoyle.contains(theAdventuresOfSherlockHolmes));
        System.out.println("5  : " + booksByArthurConanDoyle.contains(aStudyInScarlet));

        /*
        Did I mention that he's also really generous person? Bill tries to open membership into his precious library.
        He wants people to be inspired by those fiction books, just like him. However, he don't want public know about
        this (it's still his precious, after all), so he open the membership just for two people: John and Jane.
        */
        Member john = fictionBookLibrary.createMember("John");
        Member jane = fictionBookLibrary.createMember("Jane");

        /*
        Really happy to be a member of Bill's library, John immediately borrows the first series of 'Harry Potter' and
        'The Lord of the Rings'.
        */
        john.borrow(harryPotterAndTheSorcerersStone);
        john.borrow(theFellowshipOfTheRing);

        /*
        Bill knows about this, of course. Somehow, it makes him feel happy. He feels like his life is now complete.
        */
        java.util.List<Book> borrowedByJohn = john.listBorrowedBooks();

        System.out.println("6  : " + borrowedByJohn.contains(harryPotterAndTheSorcerersStone));
        System.out.println("7  : " + borrowedByJohn.contains(theFellowshipOfTheRing));

        /*
        The other member, Jane, is a hardcore fan of 'The Lord of the Rings'. The next morning, she tries to borrow two
        'The Fellowship of The Ring' and 'The Two Towers' from the series, ..
        */
        jane.borrow(theFellowshipOfTheRing);
        jane.borrow(theTwoTowers);

        /*
        .. But little did she know that 'The Fellowship of the Ring' already lent. "Well", she thought, "at least I got
        'The Two Towers'".
        */
        java.util.List<Book> borrowedByJane = jane.listBorrowedBooks();

        System.out.println("8  : " + ! borrowedByJane.contains(theFellowshipOfTheRing));
        System.out.println("9  : " + borrowedByJane.contains(theTwoTowers));

        /*
        Bill, who really cares about his customers, feels sad about this. "I should find more books", he thought. Later
        did he know that it will be really reallyh ard for him to find more rare fiction book.
        Fast forward ten years. Bill arrived at Krakhosia, a country that most people never heard of. He got there
        without any intention finding a rare fiction book. That's why his eye bewildered when he encountered another
        copy of 'The Adventures of Sherlock Holmes', ..
        */
        Book anotherTheAdventuresOfSherlockHolmes = new Book("The Adventures of Sherlock Holmes", "Arthur Conan Doyle");

        /*
        .. But because of his old age, he can't remember whether he already has it in his library, and so he checks..
        */
        System.out.println("10 : " + availableBooks.contains(anotherTheAdventuresOfSherlockHolmes));

        /*
        "Seems I had it already. Sigh..", he said. "Perhaps another time"..
        */
    }
}

class FictionBookLibrary {
    private java.util.List<Book> bookList;
    private java.util.List<Member> memberList;
    
    public FictionBookLibrary()
    {
        this.bookList = new MyArrayList();
        this.memberList = new java.util.ArrayList<>();
    }
    
    public void addCollection(Book book, int count) {
        book.setStock(book.getStock()+count);
        this.getBookList().add(book);
    }

    public java.util.List<Book> listAvailableBooks() {
        
        return this.getBookList();
    }

    public java.util.List<Book> listBooksAuthoredBy(String author) {
        java.util.List<Book>listBookByAuthor = new java.util.ArrayList<>();
        this.getBookList().stream().filter((book) -> (book.getAuthor().equals(author))).forEach((book) -> {
            listBookByAuthor.add(book);
        });
        return listBookByAuthor;
    }

    public Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        
        this.getMemberList().add(member);
        return member;
    }
    public java.util.List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(java.util.List<Book> bookList) {
        this.bookList = bookList;
    }

 
    public java.util.List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(java.util.List<Member> memberList) {
        this.memberList = memberList;
    }
    
    private class MyArrayList extends java.util.ArrayList{
        @Override
        public boolean contains(Object o){
            Book book = (Book) o;
            java.util.List<Book> list = this.subList(0, this.size()-1);
            return list.stream().anyMatch((b) -> (b.getAuthor().equals(book.getAuthor()) && b.getTitle().equals(book.getTitle())));
        }
    }
    
}

class Book {
    
    private String title;
    private String author;
    private int stock;
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
}

class Member {
    private String name;
    private java.util.List<Book> borrowedBooks;
    
    public Member(){
        this.borrowedBooks = new java.util.ArrayList<>();
    }
    
    public void borrow(Book book) {
        if(book.getStock() > 0){
            book.setStock(book.getStock()-1);
            this.getBorrowedBooks().add(book);
        }
    }

    public java.util.List<Book> listBorrowedBooks() {
        return this.getBorrowedBooks();
    }

  
    public String getName() {
        return name;
    }

 
    public void setName(String name) {
        this.name = name;
    }

    public java.util.List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(java.util.List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    
}

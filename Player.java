


  public class Player {

    int jerseynumber;
    String name;
    String year;
    public Player(int jerseynumber, String name, String year) {
      this.jerseynumber=jerseynumber;
      this.name=name;
      this.year=year;

    }
    
    public Player(){
      this.jerseynumber = -1;
      this.name = "";
      this.year = "";
  }
    
    public int getJerseynumber() {
      return jerseynumber;
    }

    public String getName() {
      return name;
    }

    public String getYear() {
      return year;
    }
    
    public void setName(String name) {
      this.name = name;
  }

  public void setJerseyNumber(int jerseyNumber) {
      this.jerseynumber = jerseyNumber;
  }

  public void setYear(String year) {
      this.year = year;
  }

    public int compareTo(Player a) {
      int number=this.jerseynumber;
      if(number>a.jerseynumber)
        return 1;
      if(number<a.jerseynumber)
        return -1;

      return 0;
    }
    public String  toString() {


      String number = Integer.toString(jerseynumber);

      return number+" "+name+" "+ year;
    }

  }

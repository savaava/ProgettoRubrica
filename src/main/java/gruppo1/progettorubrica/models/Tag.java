package gruppo1.progettorubrica.models;

public class Tag {
    private final int id;
    private String descrizione;
    private static int index = 1;

    public Tag(String descrizione) {
        this.descrizione = descrizione;
        this.id = index++;
    }

    public int getId() {
        return id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public static void setIndex(int index) {
        Tag.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this == o) return true;
        if(this.getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return this.id == tag.id || this.descrizione.equals(tag.descrizione);
    }
}

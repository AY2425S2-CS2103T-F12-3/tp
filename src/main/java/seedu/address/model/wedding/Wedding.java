package seedu.address.model.wedding;

import java.util.Date;

import seedu.address.model.person.UniquePersonList;

public class Wedding {
    private final Date weddingDate;
    private final String weddingVenue;
    
    private final UniquePersonList guests = new UniquePersonList();
    



    public Wedding(Date weddingDate, String weddingVenue) {
        this.weddingDate = weddingDate;
        this.weddingVenue = weddingVenue;
    }

    public Date getWeddingDate() {
        return weddingDate;
    }

    public String getWeddingVenue() {
        return weddingVenue;
    }

}

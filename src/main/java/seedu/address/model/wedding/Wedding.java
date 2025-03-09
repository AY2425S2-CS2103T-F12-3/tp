package seedu.address.model.wedding;

import java.util.Date;

import seedu.address.model.person.UniquePersonList;

/**
 * Overarching wedding class
 */
public class Wedding {
    private final Date weddingDate;
    private final String weddingVenue;
    private final UniquePersonList guests = new UniquePersonList();

    /**
     * Constructs a wedding object
     * @param weddingDate the date of the wedding
     * @param weddingVenue the venue of the wedding
     */
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

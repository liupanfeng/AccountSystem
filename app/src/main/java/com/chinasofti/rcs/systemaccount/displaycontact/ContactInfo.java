package com.chinasofti.rcs.systemaccount.displaycontact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ContactInfo {

    private List<PhoneNumber> itsPhoneNumbers = new ArrayList<PhoneNumber>();
    private String itsDisplayName;
    public void setDisplayName(String displayName)
    {
        this.itsDisplayName = displayName;
    }

    public String getDisplayName()
    {
        return itsDisplayName;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber)
    {
        this.itsPhoneNumbers.add(phoneNumber);
    }

    public List<PhoneNumber> getPhoneNumbers()
    {
        return itsPhoneNumbers;
    }

}

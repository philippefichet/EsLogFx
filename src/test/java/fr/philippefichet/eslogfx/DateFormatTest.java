/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.philippefichet.eslogfx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author philippefichet
 */
public class DateFormatTest {
    @Test
    @Ignore
    public void dateTime() throws ParseException {
        String dateValue = "2015-05-30T19:15:01.798+0200";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        Date date = sdf.parse(dateValue);
        assertEquals(date.getTime(), 0);
    }
}

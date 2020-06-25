package com.greycells.dateone.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateUtil
{
    private static final String D_M_YYYY = "d-MMMMM-yyyy";
    private static final String DATE_SEPARATOR = "-";

    public static Date getDate( Integer dd, Integer mm, Integer yyyy )
    {
        if ( dd == null || mm == null || yyyy == null )
            return null;

        Date retVal = null;
        try
        {
            retVal = DateTimeFormat.getFormat( D_M_YYYY ).parse( dd + DATE_SEPARATOR + mm + DATE_SEPARATOR + yyyy );
        }
        catch ( Exception e )
        {
            retVal = null;
        }

        return retVal;
    }

    public static String getDayAsString( Date date )
    {
        return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[0];
    }

    public static String getMonthAsString( Date date )
    {
        return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[1];
    }
    
    public static Integer getMonthAsInt( Date date )
    {
        return ( date == null ) ? null : Integer.parseInt(DateTimeFormat.getFormat( "d-MM-yyyy" ).format( date ).split( DATE_SEPARATOR )[1]);
    }

    public static String getYearAsString( Date date )
    {
        return ( date == null ) ? null : DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[2];
    }
    
    public static Integer getYearAsInt( Date date )
    {
        return ( date == null ) ? null : Integer.parseInt(DateTimeFormat.getFormat( D_M_YYYY ).format( date ).split( DATE_SEPARATOR )[2]);
    }

    public static boolean isValidDate( Integer dd, Integer mm, Integer yyyy )
    {
        boolean isvalidDate = true;

        try
        {
            String transformedInput = DateTimeFormat.getFormat( D_M_YYYY ).format( getDate( dd, mm, yyyy ) );
            String originalInput = dd + DATE_SEPARATOR + mm + DATE_SEPARATOR + yyyy;

            isvalidDate = transformedInput.equals( originalInput );
        }
        catch ( Exception e )
        {
            isvalidDate = false;
        }

        return isvalidDate;
    }
}
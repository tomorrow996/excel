package com.cd.tech.report.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * @author miao.yang susing@gmail.com
 * @since 2015/1/28.16:29
 */
public class Phone {

    private static final PhoneNumberUtil util = PhoneNumberUtil.getInstance();

    private final int countryCode;
    private final long nationalNumber;

    public Phone(int countryCode, long nationalNumber) {
        this.countryCode = countryCode;
        this.nationalNumber = nationalNumber;
    }


    public int getCountryCode() {
        return countryCode;
    }

    public long getNationalNumber() {
        return nationalNumber;
    }

    public String toString() {
        return normalizeToView(new Phonenumber.PhoneNumber().setCountryCode(countryCode).setNationalNumber(nationalNumber));
    }

    private static final Phonenumber.PhoneNumber parsePhoneNumber(String phone) {
        try {
            return util.parse(phone, "CN");
        } catch (NumberParseException e) {
            throw new NumberFormatException("invalid phone number: " + phone);
        }
    }
    private static final String normalizeToView(Phonenumber.PhoneNumber pn) {
        return util.formatNumberForMobileDialing(pn, "CN", true);
    }
    public static final Phone parse(String phone) {
        Phonenumber.PhoneNumber pn = parsePhoneNumber(phone);
        return new Phone(pn.getCountryCode(), pn.getNationalNumber());
    }

    public static final String normalizeToStore(String phone) {
        try {
            return util.formatNumberForMobileDialing(util.parse(phone, "CN"), "CN", false);
        } catch (NumberParseException e) {
            return null;
        }
    }

    public static final String normalizeToView(String phone) {
        try {
            return normalizeToView(parsePhoneNumber(phone));
        } catch (NumberFormatException e) {
            return phone;
        }
    }

    public static void main(String[] args) throws NumberParseException {

        System.out.println(normalizeToStore("008618612483515"));
        System.out.println(normalizeToStore("+86-186-1248-3515"));
        System.out.println(normalizeToStore("186 12483515"));
        System.out.println(normalizeToStore("+1 (646) 3641234"));
        System.out.println(normalizeToStore("00852-28113620"));
        System.out.println(normalizeToStore("+1 345-938-5326"));

        System.out.println("===");

        System.out.println(normalizeToView("008618612483515"));
        System.out.println(normalizeToView("4008123123"));
        System.out.println(normalizeToView("186 1248 3515"));
        System.out.println(normalizeToView("+864393749385"));
        System.out.println(normalizeToView("+1 (646) 3641234"));
        System.out.println(normalizeToView("00852-28113620"));
        System.out.println(normalizeToView("+1 345-938-5326"));



        System.out.println(util.format(parsePhoneNumber("04393749385"), PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
    }

}
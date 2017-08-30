package com.cd.tech.report.util.text;

/**
 * Created by dennislee on 13-12-6.
 */
public interface Normalize {
    public String normalize(CharSequence charSequence);

    public String normalizeForSmsFilter(CharSequence charSequence, boolean dropBlank);
}
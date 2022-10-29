package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Locale;

public final class CachingResponse implements HttpServletResponse {

    private final PrintWriter writer;
    private final StringBuffer contents;
    private int status;

    public CachingResponse() {
        StringWriter str = new StringWriter(4096);
        writer = new PrintWriter(str);
        contents = str.getBuffer();
    }

    public void assertContents(String expected) {
        assert contents.toString().replaceAll("[\n\r]", "").equals(expected);
    }

    public void assertContains(String expected) {
        assert contents.toString().replaceAll("[\n\r]", "").contains(expected);
    }

    public void assertStatus(int status) {
        assert getStatus() == status;
    }

    @Override
    public void addCookie(final Cookie cookie) {

    }

    @Override
    public boolean containsHeader(final String s) {
        return false;
    }

    @Override
    public String encodeURL(final String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(final String s) {
        return null;
    }

    @Override
    public String encodeUrl(final String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(final String s) {
        return null;
    }

    @Override
    public void sendError(final int i, final String s) throws IOException {

    }

    @Override
    public void sendError(final int i) throws IOException {

    }

    @Override
    public void sendRedirect(final String s) throws IOException {

    }

    @Override
    public void setDateHeader(final String s, final long l) {

    }

    @Override
    public void addDateHeader(final String s, final long l) {

    }

    @Override
    public void setHeader(final String s, final String s1) {

    }

    @Override
    public void addHeader(final String s, final String s1) {

    }

    @Override
    public void setIntHeader(final String s, final int i) {

    }

    @Override
    public void addIntHeader(final String s, final int i) {

    }

    @Override
    public void setStatus(final int i) {
        this.status = i;
    }

    @Override
    public void setStatus(final int i, final String s) {
        setStatus(i);
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getHeader(final String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(final String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    @Override
    public void setCharacterEncoding(final String s) {

    }

    @Override
    public void setContentLength(final int i) {

    }

    @Override
    public void setContentLengthLong(final long l) {

    }

    @Override
    public void setContentType(final String s) {

    }

    @Override
    public void setBufferSize(final int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(final Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}

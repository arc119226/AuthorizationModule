package com.arcare.oauth.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7019529932623153384L;

    /**
     * 預設顯示資料筆數
     */

    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 頁面顯示資料筆數。
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 當前第一筆資料的index。
     */
    private Long currentIndex;

    /**
     * 資料總筆數。
     */
    private Long totalCount;

    /**
     * 查詢結果。
     */
    private List<T> result;

    /**
     * 預設建構子。
     */
    public Page() {
        this(new Long(0), DEFAULT_PAGE_SIZE, new Long(0), new ArrayList<T>());
    }

    /**
     * 建構子。
     *
     * @param currentIndex 當前第一筆資料的index。
     * @param pageSize 頁面顯示資料筆數。
     * @param totalCount 資料總筆數。
     * @param result 查詢結果。
     */
    public Page(Long currentIndex, Integer pageSize, Long totalCount, List<T> result) {
        this.currentIndex = currentIndex;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.result = result;
    }

    /**
     * 取得頁面顯示資料筆數。
     *
     * @return 頁面顯示資料筆數。
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 設定頁面顯示資料筆數。
     *
     * @param pageSize 欲設定的頁面顯示資料筆數。
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 取得資料總筆數。
     *
     * @return 資料總筆數。
     */
    public Long getTotalCount() {
        return totalCount;
    }

    /**
     * 設定資料總筆數。
     *
     * @param totalCount 欲設定的資料總筆數。
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 取得查詢結果。
     *
     * @return 查詢結果。
     */
    public List<T> getResult() {
        return result;
    }

    /**
     * 設定查詢結果。
     *
     * @param result 欲設定的查詢結果。
     */
    public void setResult(List<T> result) {
        this.result = result;
    }

    /**
     * 取得當前第一筆資料的index。
     *
     * @return 當前第一筆資料的index。
     */
    public Long getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 設定當前第一筆資料的index。
     *
     * @param currentIndex 欲設定的當前第一筆資料的index。
     */
    public void setCurrentIndex(Long currentIndex) {
        this.currentIndex = currentIndex;
    }

    /**
     * 取得總頁數。
     *
     * @return Long 總頁數。
     */
    public Long getTotalPageCount() {
        if (totalCount % pageSize == 0) {
            return totalCount / pageSize;
        } else {
            return (totalCount / pageSize) + 1;
        }
    }

    /**
     * 取得當前頁碼，起始值為1。
     *
     * @return 當前頁碼。
     */
    public Long getCurrentPageNo() {
        return (currentIndex / pageSize) + 1;
    }

    /**
     * 取得前一頁的頁碼。
     *
     * @return 前一頁的頁碼。
     */
    public Long getPreviousPageNo() {
        Long currentPageNo = getCurrentPageNo();
        if (isPreviousPage()) {
            return currentPageNo - 1L;
        }
        return currentPageNo;
    }

    /**
     * 取得下一頁的頁碼。
     *
     * @return 下一頁的頁碼。
     */
    public Long getNextPageNo() {
        Long currentPageNo = getCurrentPageNo();
        if (!isNextPage()) {
            return currentPageNo;
        }
        return currentPageNo + 1L;
    }

    /**
     * 是否有前一頁。
     *
     * @return true為有前一頁, false則無前一頁。
     */
    public boolean isPreviousPage() {
        return this.getCurrentPageNo() > 1;
    }

    /**
     * 是否有下一頁。
     *
     * @return true為有下一頁, false則無下一頁。
     */
    public boolean isNextPage() {
        return this.getCurrentPageNo() < (this.getTotalPageCount());
    }

   /**
    * 取得指定頁數的第一筆資料的index。
    *
    * @param pageNo 指定頁數。
    * @return 指定頁數的第一筆資料的index。
    */
    protected static Integer getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 取得指定頁數的第一筆資料的index。
     *
     * @param pageNo pageNo 指定頁數。
     * @param pageSize 頁面顯示資料筆數。
     * @return 指定頁數的第一筆資料的index。
     */
    public static Integer getStartOfPage(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 取得指定頁數的第一筆資料的index。
     * @param pageNo 指定頁數。
     * @param pageSize 頁面顯示資料筆數。
     * @param totalCount 查詢總筆數。
     * @return 指定頁數的第一筆資料的index。
     */
    public static Integer getStartOfPage(int pageNo, int pageSize, int totalCount) {
        int totalPage = 0;
        int pgNo = 1;
        //如果總筆數為0則直接回傳0
        if (totalCount == 0) {
            return 0;
        }
        //計算總頁數
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = (totalCount / pageSize) + 1;
        }
        //判斷筆數
        if (pageNo == 0) {
            pgNo = 1;
        }   //用-1表示要查最後一頁
        else if (pageNo == -1) {
            pgNo = totalPage;
        }   //如果要查的頁數大於總頁數
        else if (pageNo > totalPage){
            pgNo = totalPage;
        }
        else {
            pgNo = pageNo;
        }
        return (pgNo - 1) * pageSize;
    }

    /**
     * 是否有上十頁
     *
     * @return
     */
    public boolean isPrevious10Page() {
        return getBeginPageNo() > 10 ? true : false;
    }

    /**
     * 是否有下十頁
     *
     * @return
     */
    public boolean isNext10Page() {
        return getEndPageNo() < getTotalPageCount() ? true : false;
    }

    /**
     * 十頁的第一頁頁碼
     *
     * @return
     */
    public long getBeginPageNo() {
        if (0 == getCurrentPageNo() % 10) {
            return getCurrentPageNo() - 10 + 1;
        }
        return getCurrentPageNo() - getCurrentPageNo() % 10 + 1;
    }

    /**
     * 十頁的最後一頁頁碼
     *
     * @return
     */
    public long getEndPageNo() {
        return getBeginPageNo() + 9 < getTotalPageCount() ? getBeginPageNo() + 9 : getTotalPageCount();
    }

    /**
     * 取得當前頁碼之起始筆數
     *
     * @return long
     */
    public long getBeginPageIndex() {
        return this.currentIndex + 1;
    }

    /**
     * 取得當前頁碼之結束筆數
     *
     * @return long
     */
    public long getEndPageIndex() {
        long endPageIndex = this.currentIndex + this.pageSize;
        if (endPageIndex > totalCount) {
            endPageIndex = totalCount;
        }

        return endPageIndex;
    }
}

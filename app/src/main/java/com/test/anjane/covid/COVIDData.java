package com.test.anjane.covid;

public class COVIDData {

    private String name; //지역 이름
    private String accumulated; //누적 확진자
    private String today; //오늘 확진자
    private String isolation; //격리 중
    private String release; //격리 해제
    private String eath; //사망자

    public String getAll() { return ("누적 확진자: " + accumulated + "\n오늘 확진자: " + today + "\n격리중: " + isolation + "\n격리 해제: " + release + "\n사망자: " + eath); }

    public String getEath() {
        return eath;
    }

    public void setEath(String eath) {
        this.eath = eath;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getIsolation() {
        return isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(String ratio) {
        this.accumulated = ratio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

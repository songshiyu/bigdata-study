package com.bigdata.bean;


import scala.Serializable;
import scala.math.Ordered;

public class Secondry implements Ordered<Secondry>, Serializable {

    private String first;
    private int second;

    public Secondry(String first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int compare(Secondry that) {
        if (this.first.hashCode() - that.getFirst().hashCode() != 0){
            return this.first.hashCode() - that.getFirst().hashCode();
        }else{
            return this.second - that.getSecond();
        }
    }

    public boolean $less(Secondry that) {
        if(this.first.hashCode() < that.getFirst().hashCode()){
            return true;
        }else if (this.first.hashCode() == that.getFirst().hashCode() && this.second < that.getSecond()){
            return true;
        }
        return false;
    }

    public boolean $greater(Secondry that) {
        if(this.first.hashCode() > that.getFirst().hashCode()){
            return true;
        }else if (this.first.hashCode() == that.getFirst().hashCode() && this.second > that.getSecond()){
            return true;
        }
        return false;
    }

    public boolean $less$eq(Secondry that) {
        if(this.$less(that)){
            return true;
        }else if(this.first.hashCode() == that.getFirst().hashCode() && this.second < that.getSecond()){
            return true;
        }
        return false;
    }

    public boolean $greater$eq(Secondry that) {
        if (this.$greater(that)){
            return true;
        }else if (this.first.hashCode() == that.getFirst().hashCode() && this.second > that.getSecond()){
            return true;
        }
        return false;
    }

    public int compareTo(Secondry that) {
        if (this.first.hashCode() - that.getFirst().hashCode() != 0){
            return this.first.hashCode() - that.getFirst().hashCode();
        }else {
            return this.second - that.getSecond();
        }
    }

}

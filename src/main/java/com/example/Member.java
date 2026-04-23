package com.example;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberID;
    private String name;
    private List<Loan> loanHistory;

    public Member(String name, String memberId){
        this.name = name;
        this.memberID = memberId;
        this.loanHistory = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public String getMemberID(){
        return memberID;
    }

    public void addLoan(Loan loan){
        loanHistory.add(loan);
    }

    public List<Loan> getActiveLoans(){
        ArrayList<Loan> activeLoans = new ArrayList<Loan>();
        for(Loan o: loanHistory){
            if(!(o.isReturned())){
                activeLoans.add(o);
            }
        }

        return activeLoans;
    }

    public List<Loan> getLoanHistory(){
        return loanHistory;
    }

    public String toString(){
        return "Member Name: "+name+"\n"+
        "Member ID: "+memberID;
    }
}

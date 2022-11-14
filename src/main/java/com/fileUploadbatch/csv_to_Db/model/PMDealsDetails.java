package com.fileUploadbatch.csv_to_Db.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PM_Deals_Details")
public class PMDealsDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "PM_Deal_Id")
    private String pMDealId;

    @Column(name = "Game_ID")
    private String gameId;

    @Column(name = "Area_ID")
    private String  areaId;

    @Column(name = "PMS_Purchased")
    private int pMSPurchased;

    public PMDealsDetails() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getpMDealId() {
        return pMDealId;
    }

    public void setpMDealId(String pMDealId) {
        this.pMDealId = pMDealId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String  areaId) {
        this.areaId = areaId;
    }

    public int getpMSPurchased() {
        return pMSPurchased;
    }

    public void setpMSPurchased(int pMSPurchased) {
        this.pMSPurchased = pMSPurchased;
    }
}

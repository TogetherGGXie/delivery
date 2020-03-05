package com.delivery.main.common.persistence.template.modal;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author null123
 * @since 2020-03-05
 */
@TableName("location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lng;
    private String lat;
    /**
     * 地点名称
     */
    private String title;
    /**
     * 地点地址
     */
    private String address;
    @TableId("location_id")
    private Integer locationId;


    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "Location{" +
        ", lng=" + lng +
        ", lat=" + lat +
        ", title=" + title +
        ", address=" + address +
        ", locationId=" + locationId +
        "}";
    }
}

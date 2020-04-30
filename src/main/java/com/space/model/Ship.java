package com.space.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Root
 */
@Entity
@Table(name = "ship")
@NamedQueries({
        @NamedQuery(name = "Ship.findAll", query = "SELECT s FROM Ship s"),
        @NamedQuery(name = "Ship.findById", query = "SELECT s FROM Ship s WHERE s.id = :id"),
        @NamedQuery(name = "Ship.findByName", query = "SELECT s FROM Ship s WHERE s.name = :name"),
        @NamedQuery(name = "Ship.findByPlanet", query = "SELECT s FROM Ship s WHERE s.planet = :planet"),
        @NamedQuery(name = "Ship.findByShipType", query = "SELECT s FROM Ship s WHERE s.shipType = :shipType"),
        @NamedQuery(name = "Ship.findByProdDate", query = "SELECT s FROM Ship s WHERE s.prodDate = :prodDate"),
        @NamedQuery(name = "Ship.findByIsUsed", query = "SELECT s FROM Ship s WHERE s.isUsed = :isUsed"),
        @NamedQuery(name = "Ship.findBySpeed", query = "SELECT s FROM Ship s WHERE s.speed = :speed"),
        @NamedQuery(name = "Ship.findByCrewSize", query = "SELECT s FROM Ship s WHERE s.crewSize = :crewSize"),
        @NamedQuery(name = "Ship.findByRating", query = "SELECT s FROM Ship s WHERE s.rating = :rating")})
public class Ship implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "planet",length = 50)
    private String planet;

    @Column(name = "shipType")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    @Column(name = "prodDate")
    @Temporal(TemporalType.DATE)
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;

    public Ship() {
        this.isUsed = false;
    }

    public Ship(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ship)) {
            return false;
        }
        Ship other = (Ship) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ship[ id=" + id + " name "+ name +" ]";
    }



}


package com.space.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ShipValidation {
    public static Ship validate(Map<String,String> map){
        if (map==null || map.size()==0) return  null;
        Ship ship = new Ship();
        if (map.containsKey("name")){
            String name = map.get("name");
            if (name.isEmpty() || name.length()>50)  return null;
            ship.setName(name);
        } else {
            return null;
        }
        if (map.containsKey("planet")){
            String planet =  map.get("planet");
            if (planet.isEmpty() || planet.length()>50)  return null;
            ship.setPlanet(planet);
        } else {
            return null;
        }
        if (map.containsKey("isUsed")){
            boolean isUsed = map.get("isUsed").equalsIgnoreCase("true") ? true:false;
            ship.setIsUsed(isUsed);
        }
        if (map.containsKey("prodDate")){
            Long prodDateL = Long.parseLong(map.get("prodDate"));
            if (prodDateL<0)  return null;
            Date prodDate = new Date(prodDateL);
            int year = getYear(prodDate);
            if (year>3019 || year<2800) return  null;
            ship.setProdDate(prodDate);
        } else {
            return  null;
        }
        if (map.containsKey("shipType")){
            String name =  map.get("shipType");
            ShipType shipType = ShipType.valueOf(name.toUpperCase());
            ship.setShipType(shipType);
        } else {
            return  null;
        }
        if (map.containsKey("speed")){
            double speed = Double.parseDouble(map.get("speed"));
            if (speed<=0 || speed>1) return  null;
            speed = round(speed,2);
            ship.setSpeed(speed);
        }else  {
            return  null;
        }
        if (map.containsKey("crewSize")){
           String param = map.get("crewSize");
           if (param.isEmpty()) return  null;
           long crewSize = Long.parseLong(param);
           if (crewSize<1 || crewSize>9999) return  null;
           ship.setCrewSize((int)crewSize);
        } else return  null;
        prepare(ship);
        return  ship;

    }

    public static Ship validateUpdate (Map<String,String> map, Ship old){
       if (map==null || map.size()==0) return  null;
        Ship ship = old;
        if (map.containsKey("name")){
            String name = map.get("name");
            if (name.isEmpty() || name.length()>50)  return null;
            ship.setName(name);
        }
        if (map.containsKey("planet")){
            String planet = map.get("planet");
            if (planet.isEmpty() || planet.length()>50)  return null;
            ship.setPlanet(planet);
        }
        if (map.containsKey("isUsed")){
            boolean isUsed = map.get("isUsed").equalsIgnoreCase("true")? true : false;
            ship.setIsUsed(isUsed);
        }
        if (map.containsKey("prodDate")){
            Long prodDateL = Long.parseLong(map.get("prodDate"));
            if (prodDateL<0)  return null;
            Date prodDate = new Date(prodDateL);
            int year = getYear(prodDate);
            if (year>3019 || year<2800) return  null;
            ship.setProdDate(prodDate);
        }
        if (map.containsKey("shipType")){
            String name =  map.get("shipType");
            ShipType shipType = ShipType.valueOf(name.toUpperCase());
            ship.setShipType(shipType);
        }
        if (map.containsKey("speed")){
            double speed =  Double.parseDouble(map.get("speed"));
            if (speed<=0 || speed>1) return  null;
            speed = round(speed,2);
            ship.setSpeed(speed);
        }
        if (map.containsKey("crewSize")){
            long crewSize = Long.parseLong(map.get("crewSize"));
            if (crewSize<1 || crewSize>9999) return  null;
            ship.setCrewSize((int)crewSize);
        }
        prepare(ship);
        return  ship;

    }


    public  static void prepare(Ship ship){
        float k = ship.getIsUsed() ? 0.5f : 1;
        int y0 = 3019;
        int y = getYear(ship.getProdDate());
        ship.setRating(round((80 * ship.getSpeed() * k / (y0 - y + 1)), 2));
    }



    private static  int getYear(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        return cal.get(Calendar.YEAR);
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

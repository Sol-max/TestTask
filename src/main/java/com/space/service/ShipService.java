package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.PrePersist;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ShipService {
    @Autowired
    ShipRepository repository;

    public List<Ship> findBySpec(Specification specification) {
        return repository.findAll(specification);

    }

    private Page<Ship> findBase(Map<String, String> allParams) {
        int pageNumber = 0;
        int pageSize = 3;
        ShipOrder order = null;
        try {
            pageNumber = Integer.parseInt(allParams.get("pageNumber"));
        } catch (Exception e) {
            pageNumber = 0;
        }
        try {
            pageSize = Integer.parseInt(allParams.get("pageSize"));
        } catch (Exception e) {
            pageSize = 3;
        }
        if (allParams.containsKey("order")) {
            order = ShipOrder.valueOf(allParams.get("order"));
        }
        Pageable pageable;
        if (order != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
            System.out.println("sort by " + order.getFieldName());
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }

        Specification<Ship> spec = Specification.where(id());

        if (allParams.containsKey("name")) {
            String name = allParams.get("name");
            name = HtmlUtils.htmlUnescape(name);
            spec = spec.and(nameContains(name));
        }
        if (allParams.containsKey("planet")) {
            String name = allParams.get("planet");
            name = HtmlUtils.htmlUnescape(name);
            spec = spec.and(planetContains(name));
        }

        if (allParams.containsKey("shipType")) {
            String name = allParams.get("shipType").toUpperCase();
            if (!name.equalsIgnoreCase("any")) {
                ShipType type = ShipType.valueOf(name);
                spec = spec.and(shipContains(ShipType.valueOf(name)));
            }
        }

        if (allParams.containsKey("minSpeed") && allParams.containsKey("maxSpeed")) {
            String min = allParams.get("minSpeed");
            String max = allParams.get("maxSpeed");
            try {
                double minSpeed = Double.parseDouble(min);
                double maxSpeed = Double.parseDouble(max);
                spec = spec.and(minmaxSpeedContains(minSpeed, maxSpeed));
            } catch (Exception ex) {

            }

        } else if (allParams.containsKey("minSpeed")) {
            String name = allParams.get("minSpeed");
            try {
                double speed = Double.parseDouble(name);
                spec = spec.and(minSpeedContains(speed));
            } catch (Exception ex) {

            }


        } else if (allParams.containsKey("maxSpeed")) {
            String name = allParams.get("maxSpeed");
            try {
                double speed = Double.parseDouble(name);
                spec = spec.and(maxSpeedContains(speed));
            } catch (Exception ex) {

            }


        }

        if (allParams.containsKey("minRating") && allParams.containsKey("maxRating")) {
            String min = allParams.get("minRating");
            String max = allParams.get("maxRating");
            try {
                double minRating = Double.parseDouble(min);
                double maxRating = Double.parseDouble(max);
                spec = spec.and(minmaxRatingContains(minRating, maxRating));
            } catch (Exception ex) {
            }

        } else if (allParams.containsKey("minRating")) {
            String name = allParams.get("minRating");
            try {
                double minRating = Double.parseDouble(name);
                spec = spec.and(minRatingContains(minRating));
            } catch (Exception ex) {
            }


        } else if (allParams.containsKey("maxRating")) {
            String name = allParams.get("maxRating");
            try {
                double maxRating = Double.parseDouble(name);
                spec = spec.and(maxRatingContains(maxRating));
            } catch (Exception ex) {
            }

        }

        if (allParams.containsKey("minCrewSize") && allParams.containsKey("maxCrewSize")) {
            String min = allParams.get("minCrewSize");
            String max = allParams.get("maxCrewSize");
            try {
                int minCrewSize = Integer.parseInt(min);
                int maxCrewSize = Integer.parseInt(max);
                spec = spec.and(minmaxCrewSizeContains(minCrewSize, maxCrewSize));
            } catch (Exception ex) {
            }

        } else if (allParams.containsKey("minCrewSize")) {
            String name = allParams.get("minCrewSize");
            try {
                int minCrewSize = Integer.parseInt(name);
                spec = spec.and(minCrewSizeContains(minCrewSize));
            } catch (Exception ex) {
            }


        } else if (allParams.containsKey("maxCrewSize")) {
            String name = allParams.get("maxCrewSize");
            try {
                int maxCrewSize = Integer.parseInt(name);
                spec = spec.and(maxCrewSizeContains(maxCrewSize));
            } catch (Exception ex) {
            }


        }


        if (allParams.containsKey("after") && allParams.containsKey("before")) {
            String min = allParams.get("after");
            String max = allParams.get("before");
            try {
                long minDate = Long.parseLong(min);
                long maxDate = Long.parseLong(max);

                spec = spec.and(minmaxProdDateContains(getYear(minDate), getYear(maxDate)));
            } catch (Exception ex) {
            }

        } else if (allParams.containsKey("after")) {
            String min = allParams.get("after");
            try {
                long minDate = Long.parseLong(min);
                spec = spec.and(minProdDateContains(getYear(minDate)));
            } catch (Exception ex) {
            }

        } else if (allParams.containsKey("before")) {
            String max = allParams.get("before");
            try {
                long maxDate = Integer.parseInt(max);
                spec = spec.and(maxProdDateContains(getYear(maxDate)));
            } catch (Exception ex) {
            }
            ;

        }

        if (allParams.containsKey("isUsed")) {
            String name = allParams.get("isUsed");
            boolean isUsed = name.equalsIgnoreCase("true");
            spec = spec.and(isUsedContains(isUsed));


        }
        return repository.findAll(spec, pageable);

    }

    public long countAll(Map<String, String> allParams) {

        return findBase(allParams).getTotalElements();

    }

    public List<Ship> find(Map<String, String> allParams) {

        return findBase(allParams).getContent();
    }


    static Specification<Ship> nameContains(String name) {
        return (ship, cq, cb) -> cb.like(cb.lower(ship.get("name")), "%" + name.toLowerCase() + "%");
    }

    static Specification<Ship> planetContains(String name) {
        return (ship, cq, cb) -> cb.like(cb.lower(ship.get("planet")), "%" + name.toLowerCase() + "%");
    }

    static Specification<Ship> shipContains(ShipType type) {
        return (ship, cq, cb) -> cb.equal(ship.get("shipType"), type);
    }

    static Specification<Ship> minRatingContains(double rating) {
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("rating"), rating);
    }

    static Specification<Ship> maxRatingContains(double rating) {
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("rating"), rating);
    }

    static Specification<Ship> minmaxRatingContains(double min, double max) {
        return (ship, cq, cb) -> cb.between(ship.get("rating"), min, max);
    }

    static Specification<Ship> minSpeedContains(double speed) {
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("speed"), speed);
    }

    static Specification<Ship> maxSpeedContains(double speed) {
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("speed"), speed);
    }

    static Specification<Ship> minmaxSpeedContains(double min, double max) {
        return (ship, cq, cb) -> cb.between(ship.get("speed"), min, max);
    }

    //CrewSize
    static Specification<Ship> minCrewSizeContains(int crewSize) {
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("crewSize"), crewSize);
    }

    static Specification<Ship> maxCrewSizeContains(int crewSize) {
        return (ship, cq, cb) -> cb.lessThanOrEqualTo(ship.get("crewSize"), crewSize);
    }

    static Specification<Ship> minmaxCrewSizeContains(int min, int max) {
        return (ship, cq, cb) -> cb.between(ship.get("crewSize"), min, max);
    }

    //prodDate
    static Specification<Ship> minProdDateContains(int prodDate) {

        ///return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.<Date>get("prodDate"),prodDate);
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(cb.function("YEAR", Integer.class, ship.<Date>get("prodDate")), prodDate);
    }

    static Specification<Ship> maxProdDateContains(int prodDate) {
        return (ship, cq, cb) -> cb.lessThan(cb.function("YEAR", Integer.class, ship.get("prodDate")), prodDate);

    }

    static Specification<Ship> minmaxProdDateContains(int min, int max) {
        return (ship, cq, cb) -> cb.between(cb.function("YEAR", Integer.class, ship.get("prodDate")), min, max);
    }

    static Specification<Ship> isUsedContains(boolean isUsed) {
        return (ship, cq, cb) -> cb.equal(ship.get("isUsed"), isUsed);
    }

    static Specification<Ship> orderContains(ShipOrder order) {
        return (ship, cq, cb) -> cb.equal(ship.get("order"), order);
    }

    static int getYear(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.YEAR);

    }


    static Specification<Ship> id() {
        return (ship, cq, cb) -> cb.greaterThanOrEqualTo(ship.get("id"), 0);
    }


    public Optional<Ship> findOne(long id) {
        return repository.findById(id);

    }


    @Transactional
    public Ship save(Ship ship) {
        Ship saved = null;
        saved = repository.save(ship);
        return  saved;
    }

    @Transactional
    public void delete(Ship ship) {
        repository.delete(ship);

    }

/*
    void prepareSave(Ship s) {
        float k = s.getIsUsed() ? 0.5f : 1;
        int y0 = 3019;
        s.setSpeed(round(s.getSpeed(), 2));

        int y = getYear(s.getProdDate());
        s.setRating(round((80 * s.getSpeed() * k / (y0 - y + 1)), 2));
    }
/*
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private int getYear(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        return cal.get(Calendar.YEAR);
    }
*/
}


package vn.edu.iuh.fit.backEnd.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addId;
    private Integer country;
    private String zipcode;
    private String number;
    private String city;
    private String street;
    
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(Long addId, Integer country, String zipcode, String number, String city, String street) {
		super();
		this.addId = addId;
		this.country = country;
		this.zipcode = zipcode;
		this.number = number;
		this.city = city;
		this.street = street;
	}


    // getters and setters
	
}
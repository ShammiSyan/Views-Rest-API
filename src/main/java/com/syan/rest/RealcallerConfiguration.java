package com.syan.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syan.rest.core.Template;
import com.xeiam.dropwizard.sundial.SundialConfiguration;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

/**
 * Configuration class for our Resource which specifies environment 
 * specific parameters which are specified in yaml file.
 * 
 * @prsyan
 *
 */
public class RealcallerConfiguration extends Configuration
{
    @NotEmpty
    private String template;
    
    @NotEmpty
    private String defaultName = "Stranger";
    
	@JsonProperty
	private String message;
	
	@JsonProperty
	private int messageRepetitions;
	
	@Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();
	
	@Valid
	@NotNull
	public SundialConfiguration sundialConfiguration = new SundialConfiguration();
	
	@JsonProperty("sundial")
	public SundialConfiguration getSundialConfiguration() {

	  return sundialConfiguration;
	}
	
	@JsonProperty("sundial")
	public void setSundialConfiguration(
			SundialConfiguration sundialConfiguration) {
		this.sundialConfiguration = sundialConfiguration;
	}
	
	@JsonProperty
	public String getTemplate() {
		return template;
	}

	@JsonProperty
	public void setTemplate(String template) {
		this.template = template;
	}
	
    public Template buildTemplate() {
        return new Template(template, defaultName);
    }
    
    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

	public String getMessage() {
	return message;
	}
	
	public int getMessageRepetitions() {
	return messageRepetitions;
	}
	
	@JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }
 }

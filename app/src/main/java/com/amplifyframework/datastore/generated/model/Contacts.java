package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Contacts type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Contacts", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Contacts implements Model {
  public static final QueryField ID = field("Contacts", "id");
  public static final QueryField FIRSTNAME = field("Contacts", "firstname");
  public static final QueryField LASTNAME = field("Contacts", "lastname");
  public static final QueryField PROFESSION = field("Contacts", "profession");
  public static final QueryField EMAIL = field("Contacts", "email");
  public static final QueryField PHONE = field("Contacts", "phone");
  public static final QueryField ADDRESS = field("Contacts", "address");
  public static final QueryField TYPE = field("Contacts", "type");
  public static final QueryField COMPLETED_AT = field("Contacts", "completedAt");
  public static final QueryField USER_ID = field("Contacts", "userID");
  public static final QueryField IMAGE_URL = field("Contacts", "imageURL");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String firstname;
  private final @ModelField(targetType="String") String lastname;
  private final @ModelField(targetType="String") String profession;
  private final @ModelField(targetType="String") String email;
  private final @ModelField(targetType="String") String phone;
  private final @ModelField(targetType="String") String address;
  private final @ModelField(targetType="String") String type;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime completedAt;
  private final @ModelField(targetType="ID") String userID;
  private final @ModelField(targetType="String") String imageURL;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getFirstname() {
      return firstname;
  }
  
  public String getLastname() {
      return lastname;
  }
  
  public String getProfession() {
      return profession;
  }
  
  public String getEmail() {
      return email;
  }
  
  public String getPhone() {
      return phone;
  }
  
  public String getAddress() {
      return address;
  }
  
  public String getType() {
      return type;
  }
  
  public Temporal.DateTime getCompletedAt() {
      return completedAt;
  }
  
  public String getUserId() {
      return userID;
  }
  
  public String getImageUrl() {
      return imageURL;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Contacts(String id, String firstname, String lastname, String profession, String email, String phone, String address, String type, Temporal.DateTime completedAt, String userID, String imageURL) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.profession = profession;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.type = type;
    this.completedAt = completedAt;
    this.userID = userID;
    this.imageURL = imageURL;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Contacts contacts = (Contacts) obj;
      return ObjectsCompat.equals(getId(), contacts.getId()) &&
              ObjectsCompat.equals(getFirstname(), contacts.getFirstname()) &&
              ObjectsCompat.equals(getLastname(), contacts.getLastname()) &&
              ObjectsCompat.equals(getProfession(), contacts.getProfession()) &&
              ObjectsCompat.equals(getEmail(), contacts.getEmail()) &&
              ObjectsCompat.equals(getPhone(), contacts.getPhone()) &&
              ObjectsCompat.equals(getAddress(), contacts.getAddress()) &&
              ObjectsCompat.equals(getType(), contacts.getType()) &&
              ObjectsCompat.equals(getCompletedAt(), contacts.getCompletedAt()) &&
              ObjectsCompat.equals(getUserId(), contacts.getUserId()) &&
              ObjectsCompat.equals(getImageUrl(), contacts.getImageUrl()) &&
              ObjectsCompat.equals(getCreatedAt(), contacts.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), contacts.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFirstname())
      .append(getLastname())
      .append(getProfession())
      .append(getEmail())
      .append(getPhone())
      .append(getAddress())
      .append(getType())
      .append(getCompletedAt())
      .append(getUserId())
      .append(getImageUrl())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Contacts {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("firstname=" + String.valueOf(getFirstname()) + ", ")
      .append("lastname=" + String.valueOf(getLastname()) + ", ")
      .append("profession=" + String.valueOf(getProfession()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("phone=" + String.valueOf(getPhone()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("type=" + String.valueOf(getType()) + ", ")
      .append("completedAt=" + String.valueOf(getCompletedAt()) + ", ")
      .append("userID=" + String.valueOf(getUserId()) + ", ")
      .append("imageURL=" + String.valueOf(getImageUrl()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Contacts justId(String id) {
    return new Contacts(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      firstname,
      lastname,
      profession,
      email,
      phone,
      address,
      type,
      completedAt,
      userID,
      imageURL);
  }
  public interface BuildStep {
    Contacts build();
    BuildStep id(String id);
    BuildStep firstname(String firstname);
    BuildStep lastname(String lastname);
    BuildStep profession(String profession);
    BuildStep email(String email);
    BuildStep phone(String phone);
    BuildStep address(String address);
    BuildStep type(String type);
    BuildStep completedAt(Temporal.DateTime completedAt);
    BuildStep userId(String userId);
    BuildStep imageUrl(String imageUrl);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String firstname;
    private String lastname;
    private String profession;
    private String email;
    private String phone;
    private String address;
    private String type;
    private Temporal.DateTime completedAt;
    private String userID;
    private String imageURL;
    @Override
     public Contacts build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Contacts(
          id,
          firstname,
          lastname,
          profession,
          email,
          phone,
          address,
          type,
          completedAt,
          userID,
          imageURL);
    }
    
    @Override
     public BuildStep firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }
    
    @Override
     public BuildStep lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
    
    @Override
     public BuildStep profession(String profession) {
        this.profession = profession;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    @Override
     public BuildStep address(String address) {
        this.address = address;
        return this;
    }
    
    @Override
     public BuildStep type(String type) {
        this.type = type;
        return this;
    }
    
    @Override
     public BuildStep completedAt(Temporal.DateTime completedAt) {
        this.completedAt = completedAt;
        return this;
    }
    
    @Override
     public BuildStep userId(String userId) {
        this.userID = userId;
        return this;
    }
    
    @Override
     public BuildStep imageUrl(String imageUrl) {
        this.imageURL = imageUrl;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String firstname, String lastname, String profession, String email, String phone, String address, String type, Temporal.DateTime completedAt, String userId, String imageUrl) {
      super.id(id);
      super.firstname(firstname)
        .lastname(lastname)
        .profession(profession)
        .email(email)
        .phone(phone)
        .address(address)
        .type(type)
        .completedAt(completedAt)
        .userId(userId)
        .imageUrl(imageUrl);
    }
    
    @Override
     public CopyOfBuilder firstname(String firstname) {
      return (CopyOfBuilder) super.firstname(firstname);
    }
    
    @Override
     public CopyOfBuilder lastname(String lastname) {
      return (CopyOfBuilder) super.lastname(lastname);
    }
    
    @Override
     public CopyOfBuilder profession(String profession) {
      return (CopyOfBuilder) super.profession(profession);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder phone(String phone) {
      return (CopyOfBuilder) super.phone(phone);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder type(String type) {
      return (CopyOfBuilder) super.type(type);
    }
    
    @Override
     public CopyOfBuilder completedAt(Temporal.DateTime completedAt) {
      return (CopyOfBuilder) super.completedAt(completedAt);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder imageUrl(String imageUrl) {
      return (CopyOfBuilder) super.imageUrl(imageUrl);
    }
  }
  
}

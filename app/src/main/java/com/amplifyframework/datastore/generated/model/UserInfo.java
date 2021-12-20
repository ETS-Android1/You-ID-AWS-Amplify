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

/** This is an auto generated class representing the UserInfo type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UserInfos", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class UserInfo implements Model {
  public static final QueryField ID = field("UserInfo", "id");
  public static final QueryField FIRSTNAME = field("UserInfo", "firstname");
  public static final QueryField LASTNAME = field("UserInfo", "lastname");
  public static final QueryField EMAIL = field("UserInfo", "email");
  public static final QueryField PHONE = field("UserInfo", "phone");
  public static final QueryField AGE = field("UserInfo", "age");
  public static final QueryField COMPLETED_AT = field("UserInfo", "completedAt");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String firstname;
  private final @ModelField(targetType="String") String lastname;
  private final @ModelField(targetType="String") String email;
  private final @ModelField(targetType="String") String phone;
  private final @ModelField(targetType="String") String age;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime completedAt;
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
  
  public String getEmail() {
      return email;
  }
  
  public String getPhone() {
      return phone;
  }
  
  public String getAge() {
      return age;
  }
  
  public Temporal.DateTime getCompletedAt() {
      return completedAt;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UserInfo(String id, String firstname, String lastname, String email, String phone, String age, Temporal.DateTime completedAt) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
    this.age = age;
    this.completedAt = completedAt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UserInfo userInfo = (UserInfo) obj;
      return ObjectsCompat.equals(getId(), userInfo.getId()) &&
              ObjectsCompat.equals(getFirstname(), userInfo.getFirstname()) &&
              ObjectsCompat.equals(getLastname(), userInfo.getLastname()) &&
              ObjectsCompat.equals(getEmail(), userInfo.getEmail()) &&
              ObjectsCompat.equals(getPhone(), userInfo.getPhone()) &&
              ObjectsCompat.equals(getAge(), userInfo.getAge()) &&
              ObjectsCompat.equals(getCompletedAt(), userInfo.getCompletedAt()) &&
              ObjectsCompat.equals(getCreatedAt(), userInfo.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), userInfo.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFirstname())
      .append(getLastname())
      .append(getEmail())
      .append(getPhone())
      .append(getAge())
      .append(getCompletedAt())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UserInfo {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("firstname=" + String.valueOf(getFirstname()) + ", ")
      .append("lastname=" + String.valueOf(getLastname()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("phone=" + String.valueOf(getPhone()) + ", ")
      .append("age=" + String.valueOf(getAge()) + ", ")
      .append("completedAt=" + String.valueOf(getCompletedAt()) + ", ")
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
  public static UserInfo justId(String id) {
    return new UserInfo(
      id,
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
      email,
      phone,
      age,
      completedAt);
  }
  public interface BuildStep {
    UserInfo build();
    BuildStep id(String id);
    BuildStep firstname(String firstname);
    BuildStep lastname(String lastname);
    BuildStep email(String email);
    BuildStep phone(String phone);
    BuildStep age(String age);
    BuildStep completedAt(Temporal.DateTime completedAt);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String age;
    private Temporal.DateTime completedAt;
    @Override
     public UserInfo build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UserInfo(
          id,
          firstname,
          lastname,
          email,
          phone,
          age,
          completedAt);
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
     public BuildStep age(String age) {
        this.age = age;
        return this;
    }
    
    @Override
     public BuildStep completedAt(Temporal.DateTime completedAt) {
        this.completedAt = completedAt;
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
    private CopyOfBuilder(String id, String firstname, String lastname, String email, String phone, String age, Temporal.DateTime completedAt) {
      super.id(id);
      super.firstname(firstname)
        .lastname(lastname)
        .email(email)
        .phone(phone)
        .age(age)
        .completedAt(completedAt);
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
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder phone(String phone) {
      return (CopyOfBuilder) super.phone(phone);
    }
    
    @Override
     public CopyOfBuilder age(String age) {
      return (CopyOfBuilder) super.age(age);
    }
    
    @Override
     public CopyOfBuilder completedAt(Temporal.DateTime completedAt) {
      return (CopyOfBuilder) super.completedAt(completedAt);
    }
  }
  
}

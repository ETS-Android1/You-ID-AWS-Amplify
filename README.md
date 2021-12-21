<!DOCTYPE html>
<html>
<head>
</head>
<body>
<h1> Personal Documents Application [Android + Java + AWS Amplify]</h1>
<p>
<b> All contents in this project are solely for learning purpose. :) </b>
<br>
<br>
<div class="info"> <font color="#303030">
<h2> <b>Introduction</b> </h2>
<b> [Semester Project] </b> <br>
An Android Application to keep all your personal and identifiaiton documents handy. Will allow you to upload documetns images and contacts associated with such documetns like Doctor's contact with medical reports, Lawyer's contact with property documents etc.  
<br> <br>
Backend created using Amazon Web Services AWS Amplify [Cognito, DynamoDB, S3, Amplify]  <br>
<br><br></font></div> 
</p>
<br><br>
<br><br>
<h1>UI - Activities</h1>
<p>Basic introduction of application activities:</p>
<h2> SignIn  - ForgotPassword -  SignOut</h2>
<div class="row">
  <div class="column">
    <img src="https://i.ibb.co/smGF0Nr/1.png" alt="SignIn" width="300" height="620">
  </div>
  <div class="column">
    <img src="https://i.ibb.co/q0hf7wD/2.png" alt="ForgotPassword" width="300" height="620" >
  </div>
  <div class="column">
    <img src="https://i.ibb.co/VqWW3Jw/3.png" alt="SignUp" width="300" height="620">
  </div>
</div>
<p style="clear: both;">
<p> 
<b>Sign In</b> activity is the main activity, this is what a user will see when application start. Here user have option to sign in 
using Email and Password, from second time login, user can use his fingerprints to sign in. To use this feature, user must have his
 fingerprints set up in his device. New users can go to SignUp activity to create an account.
In case of forgot password, user can click on the <b>ForgetPassword</b> option activity will appear to get user’s email and will send password reset link to the user. 
</p>
<br>
<h2> Employer [Admin] Landing/Home Page</h2>
 <img src="https://i.ibb.co/D7vqn1x/6.png" alt="SignUp" width="300" height="620">
<p> Once the user signin, he will land on this activity which will act as Home page. From here user can navigate through icons to goto desired activity or can use Navigation Drawer</p>
<br>
<h2> Navigation Drawer [Employer]</h2>
<div class="row">
  <div class="column">
    <img src="https://i.ibb.co/CmK0c9Z/23.png" alt="SignIn" width="300" height="620">
  </div>
</div>
<p><b> Navigation Drawer</b> will display more activites to the user. Once the user will select an activity
 he will be moved there and that item/activity name on the Navigation Drawer will be hlighted. </p>
 <br>
 
<h2> Identification Page </h2>
 <img src="https://i.ibb.co/NngNp8t/5.png" alt="identification" width="300" height="620">
<p> As soon as the user will sign in, he will be asked to enter his identification first.
</p>
<br>
<h2> Health - Travel - Merchant - Legal </h2>
<div class="row"> 
<div class="column">
    <img src="https://i.ibb.co/Y0PSWCd/12.png" alt="Attendance" width="300" height="620">
  </div>
  <div class="column">
    <img src="https://i.ibb.co/99m4xsg/7.png" alt="EmployeesList" width="300" height="620">
  </div>
  <div class="column">
    <img src="https://i.ibb.co/gySY7TY/8.png" alt="Attendance" width="300" height="620">
  </div>
</div>
<h3> Categories</h3>
<p> Each category has option to uplaod images save contacts. Images and Contacts for each category will be displayed separately in each activity i.e Health Travel etc.
 </p>
<br>
<h3> Document List </h3>
<p> User can see his uploaded documents in a spinner list. 
 </p>
<br>
<h2> Download Document </h2>
<div class="row">
  <div class="column">
    <img src="https://i.ibb.co/0V9WwQR/14.png" alt="Terminate1" width="300" height="620">
  </div>
</div>
<p> To download documents, user can goto download option and there documents can be downloaded all at once by clicking the button at top or can be downloaded separately by clicking on document icon on each document. </p>


 
<h2> ICE - InCaseOfEmergency </h2>
 <div class="row">
  <div class="column">
    <img src="https://i.ibb.co/VSv7s4d/16.png" alt="Terminate1" width="300" height="620">
  </div>
</div>
<p>This activity has 3 contacts options in case of emergency and an identification document as well as medical documents. </p>


</body>
</html>



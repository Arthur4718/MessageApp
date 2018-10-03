# MessageApp
An App showcasing how to use Firebase to pass messages in real time between devices.

Firebase its a realtime database that comes with a series of products. The autentication its pretty easy to implement and it comes with a nice console for settings. The Android documentation reference is in the link below:

https://firebase.google.com/docs/android/setup?hl=pt-br

To set up your authentication, first you need to create a new project on firebase. Follow all the steps to configure the java package and get the google services Json. Once you have the Json its time to load some dependencies on your Gradle App Module.


```Java
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    testImplementation 'junit:junit:4.12'
    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support:design:27.0.2'
    implementation 'com.google.firebase:firebase-core:16.0.1'
    implementation 'com.google.firebase:firebase-database:16.0.1'
    implementation 'com.google.firebase:firebase-auth:16.0.1'


}
```
Note: Always check out for the latest packages for firebase auth and database in the google documentation. 

As highlighted by Firebase Android tutoriral, you should place de google services Json insde the App folder in your project. 

# Registering an Account to your Firebase Project

In the activity class you should set up some libraries to work with the Firebase Auth Object. 

```Java
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

```

Create a FirebaseAuth variable and later grab and instance of Firebase Auth in the OnCreate method. 

```Java
private FirebaseAuth mAuth;
```
Here a sample code from the RegisterAcvitivy.Java.
```Java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mRegisterButton = (Button) findViewById(R.id.register_sign_up_button);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirebaseUser();
            }
        });

        //Creating a new firebase instnance from a static method.
        mAuth = FirebaseAuth.getInstance();


    }

```

Using the FirebaseAuth object we call the createUserWithEmailAndPassword, sending e-mail and password as parameters and adding the OnCompleteListener to check if the task was successeful. 

```Java

mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("App", "createUser onComplete" + task.isSuccessful());
                Toast.makeText(RegisterActivity.this, "Sucesss", Toast.LENGTH_SHORT).show();

                if(!task.isSuccessful()){
                    Log.d("App", "user creation failed" + task.isSuccessful());
                    showErrorDialog("Registration attempt failed");

                }else{
                    saveDisplayName();
                    Intent finishRegistration = new Intent(RegisterActivity.this,LoginActivity.class);
                    finish();
                    startActivity(finishRegistration);
                }

            }
        });

```

# Creating an InstantMessage Class. 

In order to send messages to firebase we need to create class that can hold the userMessage and userName. 

```Java
package com.devarthur.messageapp;

public class InstantMessage {

    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstantMessage() {

    }

    public String getMessage() {
        return message;
    }

    public String getAuthor(){
        return author;
    }
}



```

# Send Message method and how to push data into firebase dabatase. 
By the time i made this project i had to create a firebase dabatse with the Test Mode active. In this mode, the read and write funcion are defined as true to all who have the firebase database URL. Since its an example project you should consider upgrading this method in order to only permit access to certain apps or services that you would like to work within your dabatase. 

Inside the MainChatActivity class we handle the user input by getting the text from the texview. Here we create a InstantMessage object with message and username as constructors. Using the FirebaseDatabase reference it is possible to push the user message object. 

```Java
private void sendMessage() {

        Log.d("App", "Message Sent");

        String userInput = mInputText.getText().toString();
        if(!userInput.equals("")){
            InstantMessage userMessage = new InstantMessage(userInput, mDisplayName);
            mDatabaseReference.child("messages").push().setValue(userMessage);
            mInputText.setText("");

        }

    }

```



package cs490.breakfastclub.CameraAndPhotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cs490.breakfastclub.UserFiles.User;

/**
 * Created by Emma on 11/1/2016.
 */
public class Photo implements Parcelable {

    private String photoName;
    private Bitmap bMap;
    private String photoUserId;
    private String photoSquadId;
    private boolean isBreakfastFeed;
    private boolean isSquadFeed;
    private boolean isProfilePhoto = false;
    private boolean isSquadProfilePhoto = false;

    public Photo(String photoName, Bitmap bMap, String photoUserId, String photoSquadId, boolean isBreakfastFeed, boolean isSquadFeed){
        this.photoName = photoName;
        this.bMap = bMap;
        this.photoUserId = photoUserId;
        this.photoSquadId = photoSquadId;
        this.isBreakfastFeed = isBreakfastFeed;
        this.isSquadFeed = isSquadFeed;
    }

    // Only to be used with newly taken photos
    public Photo(String photoName, Bitmap bMap, String photoUserId){
        this.photoName = photoName;
        this.bMap = bMap;
        this.photoUserId = photoUserId;
        this.photoSquadId = "";
        isBreakfastFeed = false;
        isSquadFeed = false;
    }

    // Only to be used with newly taken photos
    public Photo(String photoPath, String photoUserId){
        this.photoName = getNameFromPath(photoPath);
        this.bMap = getNewBitmap(photoPath);
        this.photoUserId = photoUserId;
        this.photoSquadId = "";
        isBreakfastFeed = false;
        isSquadFeed = false;
    }

    // Parcelling part
    public Photo(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);

        this.photoName = data[0];

        if(data[1] != null)
        {
            this.bMap = StringToBitMap(data[1]);
        }

        this.photoUserId = data[2];
        this.photoSquadId = data[3];
        this.isBreakfastFeed = Boolean.parseBoolean(data[4]);
        this.isSquadFeed = Boolean.parseBoolean(data[5]);

    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        String uri = null;
        String bMap = null;
        String photoUser = String.valueOf(this.photoUserId);
        String photoSquad = String.valueOf(this.photoSquadId);
        String isBreakfast = String.valueOf(this.isBreakfastFeed);
        String isSquad = String.valueOf(this.isSquadFeed);

        if(this.bMap != null)
        {
            uri = BitMapToString(this.bMap);
        }

        dest.writeStringArray(new String[]
                {
                        this.photoName,
                        bMap,
                        photoUser,
                        photoSquad,
                        isBreakfast,
                        isSquad
                });
    }
    public static final Creator CREATOR = new Creator() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };


    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public String getNameFromPath(String photoPath)
    {
        int start  = photoPath.lastIndexOf('/');
        int end  = photoPath.lastIndexOf('.');
        return photoPath.substring(start+1, end);
    }

    public Bitmap getNewBitmap( String localPhotoPath)
    {
        Bitmap bitMap = BitmapFactory.decodeFile(localPhotoPath);
        // int orientation = getOrientation(getApplicationContext(), photoUri);
        try
        {
            ExifInterface exif = new ExifInterface(localPhotoPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(), bitMap.getHeight(), matrix, true);
            bitMap = rotatedBitmap;

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return bitMap;
    }

    public void addPhotoToFirebase(final User currentUser, final String currentBreakfast)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        final String userId = currentUser.getUserId();
        //  String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        // we finally have our base64 string version of the image, save it.



        StorageReference mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://breakfastclubapp-437bd.appspot.com");
        StorageReference photoRef = mStorage.child("Photos/Breakfast1/" + getPhotoName());
        UploadTask uploadTask = photoRef.putBytes(bytes);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.v("Image Upload", "Failure Image Upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.v("Image Upload", "SuccessFul Image Upload " + downloadUrl.toString());

                //Todo: get the breakfast club key
                //       mDatabase.child("Photos/Breakfast1/" + getPhotoName()).child("image").setValue(base64Image);
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("user id").setValue(getPhotoUserId());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("squad id").setValue(getPhotoSquadId());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("isBreakfast").setValue(isBreakfastFeed());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("isSquad").setValue(isSquadFeed());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("isUserProfile").setValue(isProfilePhoto());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("isSquadProfile").setValue(isSquadProfilePhoto());
                mDatabase.child("Photos").child(currentBreakfast).child(getPhotoName()).child("image url").setValue(downloadUrl.toString());


                mDatabase.child("Users/").child(String.valueOf(userId)).child("Photos").child(currentBreakfast).child(getPhotoName()).setValue(downloadUrl.toString());


                if (isProfilePhoto()) {
                    mDatabase.child("Users").child(String.valueOf(userId)).child("profileImageUrl").setValue(downloadUrl.toString());
                    mDatabase.child("Users").child(String.valueOf(userId)).child("profileImageID").setValue(getPhotoName());
                    currentUser.setProfileImageID(getPhotoName());
                    currentUser.setProfileImageUrl(downloadUrl.toString());
                }

                if (isSquadProfilePhoto()) {
                    mDatabase.child("Squads").child(getPhotoSquadId()).child("profileImageUrl").setValue(downloadUrl.toString());
                    mDatabase.child("Squads").child(getPhotoSquadId()).child("profileImageID").setValue(getPhotoName());
                    currentUser.getSquad().setSquadImageID(getPhotoName());
                    currentUser.getSquad().setSquadImageUrl(downloadUrl.toString());
                }

                if (isBreakfastFeed()) {
                    //Todo get breakfast club key
                    mDatabase.child("Breakfasts").child(currentBreakfast).child("Photos").child(getPhotoName()).setValue(downloadUrl.toString());
                    mDatabase.child("Breakfasts").child(currentBreakfast).child("Votes").child(getPhotoName()).setValue(0);

                }

                if (isSquadFeed())
                    //Todo get breakfast club key
                    // mDatabase.child("Squads/" + getPhotoSquadId() +  "/Breakfast1/Photos").child(getPhotoName()).setValue(downloadUrl.toString());
                    mDatabase.child("Squads").child(getPhotoSquadId()).child("Photos").child(currentBreakfast).child(getPhotoName()).setValue(downloadUrl.toString());
            }
        });
    }


    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public Bitmap getbMap() {
        return bMap;
    }

    public void setbMap(Bitmap bMap) {
        this.bMap = bMap;
    }

    public String getPhotoUserId() {
        return photoUserId;
    }

    public void setPhotoUserId(String photoUserId) {
        this.photoUserId = photoUserId;
    }

    public String getPhotoSquadId() {
        return photoSquadId;
    }

    public void setPhotoSquadId(String photoSquadId) {
        this.photoSquadId = photoSquadId;
    }

    public boolean isBreakfastFeed() {
        return isBreakfastFeed;
    }

    public void setIsBreakfastFeed(boolean isBreakfastFeed) {
        this.isBreakfastFeed = isBreakfastFeed;
    }

    public boolean isSquadFeed() {
        return isSquadFeed;
    }

    public void setIsSquadFeed(boolean isSquadFeed) {
        this.isSquadFeed = isSquadFeed;
    }

    public boolean isProfilePhoto() {
        return isProfilePhoto;
    }

    public void setIsProfilePhoto(boolean isProfilePhoto) {
        this.isProfilePhoto = isProfilePhoto;
    }


    public boolean isSquadProfilePhoto() {
        return isSquadProfilePhoto;
    }

    public void setIsSquadProfilePhoto(boolean isSquadProfilePhoto) {
        this.isSquadProfilePhoto = isSquadProfilePhoto;
    }
}

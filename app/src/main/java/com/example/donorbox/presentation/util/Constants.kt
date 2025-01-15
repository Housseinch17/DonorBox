package com.example.donorbox.presentation.util

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import okio.IOException
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

object Constants {
    const val BASE_URL = "https://fcm.googleapis.com/"

    fun replaceUsername(username: String?): String? {
        return username?.replace("@", "*")?.replace(".", "_")
    }

    private const val FIREBASE_MESSAGING_SERVICE = "https://www.googleapis.com/auth/firebase.messaging"

    fun getAccessToken(): String?{
        try{
            val jsonString: String = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"donorbox-dc461\",\n" +
                    "  \"private_key_id\": \"463df40ea584812cdbfc4016d3bcdaacf7621e45\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDDVVIQt4NZIvuc\\n5BvPPjbl8dww9XfqdDCrc57l4XweVwq1XTONDGPoRJRDyWqDTQqaWyCpgnz2HCM1\\nFo5dtFSEXv3iQTkRYGHWKRhpPRUQl7MxB91T7UgmVT0T3/s6s+X0ZtMnN8dYXjBR\\nQrlU5umdVBbHAvo0mckY1rzWrsgu/03Q5Qng4BmHcCsw340xsVz7nA6Lg5Xs8/3M\\n+wGJYxLjnoHPiXB1hmnYpFWNpQXQqQxC4FZQFraCQO+i9B498voZFp7h34b41Ym3\\nU6pe0XqZgtOt8UqwHNI3xafHI44YjfBAFWzEySUuQsFXRgTBWEvibElZ2sFYgQ7z\\nYB67hQf3AgMBAAECggEAPeyoCh2redAx1UCpok9WZxqAVFMVc5HnNgt/BeCwkQvG\\nHM4RwDb721AWctEBP+PmJx4ZojN4eytbbAKdGo7HNnnI4fgMZ/pWueaoUwCm025M\\nRBB6tZclRuXB6bGZuzf6ADuy2B2CSHz/s1tEVuqniBV27RuAB7nzOzkkf6jEVMVU\\nLD6PAVmvIPmW8ZB6oDOYiVEOVCrHXEFysewXyE9HzBzzpBS8sqBeSFh+0S6kzwrJ\\nJR/DsAkhdI6fBCi+jCmjJ0vfM3E5HB95SrgmIC0GDzl5jZB4CsDloiCjpKW4CDRv\\niaE5r6N9ykMNuNFYE58AVuClk6zPC7Kja8CAJ9e2qQKBgQDuMMrKrPfJyI2mi/Pt\\nupVxfP/3KGxiZNuFH2JTjIx+48+nWsg62qvWi5p4cqA4WfyBz9SRWb+SBgnS9ata\\nD5SzqD0WC1RnMnJUBBQ3c0El2WT55EgPo7I/0R5hPt/ArtqVD3e/cQ1kFXv9qSFI\\n19smC80fE163C4pgXUlOJAz9DwKBgQDR8DJCNdoZSc7vFre1qRIKi84XNjnvgn3a\\nr5IKu89jG8AYV0oBtTvLRn+c+bt+yavUfCPz70ncJIijFwTabDuZJoTwxe2+5FpI\\np5R7dhG3qL35DBSX/m0E5KN4kRt4tw5cVC9oNFqvfG+AvqKZwcmkD/3IiuCGxHAX\\nbqeemtWWmQKBgAHh9NhhQZwgr63vUUBBx0icWlGTyjeE4I9OzjIm3D1sg4mpGAgP\\nDmiltdTZ4xZO78X+5Ik5kmAPGCjItFAD64L4A3OXJ1WhenHbjmVZzdwEud2XxlIJ\\nUzFZ78mI+6/EMgoXkzS6KgbufIpmudKfkiXc1gQ23PrJZpCYbCnn8wpVAoGAEegh\\nAVDCl6GaCv6vEMmpBklCfOxdKLbCsWKEXHIEkHmPewQHBfpFbWNIA0Wx3nCpPWiR\\nUH86l5JuLSsLeO3+b2O/tyHK05t6r/PLUHSTskysV4/WOdizx9UQtHn8E+HZUbrE\\niGjey1Ub1altCRxkbKAIj5B48kXTIcyIc5jVWxECgYEAlI1Ceg1ZkYjBlNwJaVQd\\n0Rju+2HLPqla1ETtyvVMS3ojbriTzdZtpMUHd1rk5RDh6TGPZu+GiP3qE7WvwGpH\\nGOpH12lV1Fv8E56MWgVyA42QhcqxRUHniWvO+zxMg+1O4y7qonlnwSeW4abmY+Q+\\niBc9c0hLiEXkm8bFN0nEUXs=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-72gb6@donorbox-dc461.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"114520355700864100966\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-72gb6%40donorbox-dc461.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"

            val stream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(arrayListOf(FIREBASE_MESSAGING_SERVICE))

            googleCredentials.refresh()

            return googleCredentials.accessToken.tokenValue
        }catch (e: IOException){
            Log.e("FCM", "Constants Error getting access token", e)
            return null
        }
    }
}
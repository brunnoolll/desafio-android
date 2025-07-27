package com.picpay.desafio.android.presentation.ui.userlist


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.picpay.desafio.android.domain.model.User

@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = user.img,
            contentDescription = "Avatar de ${user.name}",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "@${user.username}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

//// Manter o preview é uma boa prática para testar isoladamente
//@Preview(showBackground = true)
//@Composable
//fun UserListItemPreview() {
//    DesafioAndroidTheme {
//        Surface {
//            val sampleUser = User(
//                id = "1",
//                name = "Bruno Rodrigues",
//                username = "bruno.rodrigues",
//                imageUrl = ""
//            )
//            UserListItem(user = sampleUser, modifier = Modifier.fillMaxWidth())
//        }
//    }
//}
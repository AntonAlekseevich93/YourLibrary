import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import yourlibrary.common.resources.generated.resources.PatsySans
import yourlibrary.common.resources.generated.resources.Raleway_Bold
import yourlibrary.common.resources.generated.resources.Raleway_Medium
import yourlibrary.common.resources.generated.resources.Raleway_Regular
import yourlibrary.common.resources.generated.resources.Res

data class AppTypography(
    val title1Regular: TextStyle,
    val title1Medium: TextStyle,
    val title1Bold: TextStyle,
    val title2Regular: TextStyle,
    val title2Medium: TextStyle,
    val title2Bold: TextStyle,
    val title3Regular: TextStyle,
    val title3Medium: TextStyle,
    val title3Bold: TextStyle,
    val headlineRegular: TextStyle,
    val headlineMedium: TextStyle,
    val headlineBold: TextStyle,
    val bodyRegular: TextStyle,
    val bodyMedium: TextStyle,
    val bodyBold: TextStyle,
    val buttonRegular: TextStyle,
    val buttonMain: TextStyle,
    val buttonBold: TextStyle,
    val buttonSmall: TextStyle,
    val footnoteRegular: TextStyle,
    val footnoteRegularItalic: TextStyle,
    val footnoteMedium: TextStyle,
    val footnoteBold: TextStyle,
    val captionRegular: TextStyle,
    val captionMedium: TextStyle,
    val captionBold: TextStyle,
    val appTitle: TextStyle,
)


@Composable
fun MainTypography(): AppTypography {
//    val fontFamilyMain = FontFamily(
//        Font(Res.font.BartinaRegular, FontWeight.Normal),
//        Font(Res.font.BartinaBold, FontWeight.Bold),
//        Font(Res.font.BartinaLight, FontWeight.Light),
//        Font(Res.font.BartinaSemibold, FontWeight.SemiBold),
//        Font(Res.font.BartinaThin, FontWeight.Thin)
//    )

    val fontFamilyMain = FontFamily(
        Font(Res.font.Raleway_Regular, FontWeight.Normal),
        Font(Res.font.Raleway_Bold, FontWeight.Bold),
        Font(Res.font.Raleway_Medium, FontWeight.Medium),
    )

    return AppTypography(
        appTitle = TextStyle(
            fontFamily = FontFamily(Font(Res.font.PatsySans)),
            fontSize = 24.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title1Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title1Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title1Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title2Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title2Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title2Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title3Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title3Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        title3Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        headlineRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.30.sp,
            lineHeight = 22.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.30.sp,
            lineHeight = 22.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        headlineBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.30.sp,
            lineHeight = 22.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        bodyRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        bodyBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        buttonRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        buttonMain = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        buttonBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        buttonSmall = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        footnoteRegularItalic = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        footnoteRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        footnoteMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        footnoteBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        captionRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        captionMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        ),
        captionBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
            fontFeatureSettings = "'lnum'" //it is used to remove the font outline from the numbers
        )
    )
}


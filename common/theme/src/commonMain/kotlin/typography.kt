import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import yourlibrary.common.resources.generated.resources.KreadonBold
import yourlibrary.common.resources.generated.resources.KreadonExtraBold
import yourlibrary.common.resources.generated.resources.KreadonExtraLight
import yourlibrary.common.resources.generated.resources.KreadonLight
import yourlibrary.common.resources.generated.resources.KreadonMedium
import yourlibrary.common.resources.generated.resources.KreadonRegular
import yourlibrary.common.resources.generated.resources.Res
import yourlibrary.common.resources.generated.resources.copilot

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
        Font(Res.font.KreadonRegular, FontWeight.Normal),
        Font(Res.font.KreadonBold, FontWeight.Bold),
        Font(Res.font.KreadonLight, FontWeight.Light),
        Font(Res.font.KreadonExtraBold, FontWeight.ExtraBold),
        Font(Res.font.KreadonExtraLight, FontWeight.ExtraLight),
        Font(Res.font.KreadonMedium, FontWeight.Medium),
    )

    return AppTypography(
        appTitle = TextStyle(
            fontFamily = FontFamily(Font(Res.font.copilot)),
            fontSize = 24.sp,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
        ),
        title1Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
        ),
        title1Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
        ),
        title1Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 32.sp,
        ),
        title2Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
        ),
        title2Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
        ),
        title2Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 28.sp,
        ),
        title3Regular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
        ),
        title3Medium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
        ),
        title3Bold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
        ),
        headlineRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.45.sp,
            lineHeight = 24.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.45.sp,
            lineHeight = 24.sp,
        ),
        headlineBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.45.sp,
            lineHeight = 24.sp,
        ),
        bodyRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
        ),
        bodyBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.25.sp,
            lineHeight = 20.sp,
        ),
        buttonRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
        ),
        buttonMain = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
        ),
        buttonBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.25.sp,
            lineHeight = 20.sp,
        ),
        buttonSmall = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
        ),
        footnoteRegularItalic = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
        ),
        footnoteRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
        ),
        footnoteMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
        ),
        footnoteBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp,
            lineHeight = 16.sp,
        ),
        captionRegular = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
        ),
        captionMedium = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
        ),
        captionBold = TextStyle(
            fontFamily = fontFamilyMain,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            lineHeight = 12.sp,
        )
    )
}


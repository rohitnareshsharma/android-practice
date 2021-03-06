package com.practice;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.practice.data.repositories.TrendingRepoRepository;
import com.practice.ui.activities.TrendingReposActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.practice.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

/**
 * TrendingReposActivity Tests
 *
 * Scope :
 *
 * 1. See if title Trending is displayed properly
 *
 * 2. See if option menu is there at the top right and is clickable
 *    and carries sort by name and sort by stars option in it.
 *
 * 3. Mock network response and get the list displayed (TODO)
 *
 * 4. See if top list item is clickable and got expanded
 *
 * 5. Tap same child again. It should collapse back.
 *
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class TrendingReposActivityTest extends MockWebServerClient {

    private static final String TAG = "TrendingReposActivityTe";

    @Rule
    public ActivityTestRule<TrendingReposActivity> mTrendingReposActivityTestRule = new ActivityTestRule<TrendingReposActivity>(TrendingReposActivity.class, true, false);

    @Before
    public void setup() {

        Log.d(TAG, "setup() called");
        mServer.enqueue(getTrendingRepoMockResponse());

        // We should have injected using Dagger.
        // I need more time here to explore that
        // See MockTrendingReposViewModelModule
        // Will come back later on this part. TODO

//        DaggerTrendingReposActivityComponents.builder()
//                .trendingReposListAdapterModule(new TrendingReposListAdapterModule((LayoutInflater) mTrendingReposActivityTestRule.getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)))
//                .trendingReposViewModelModule(new MockTrendingReposViewModelModule(mTrendingReposActivityTestRule.getActivity(), getMockUrl()))
//                .build()
//                .inject(mTrendingReposActivityTestRule.getActivity());

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra(TrendingRepoRepository.EXTRA_BASE_URL, getMockUrl());

        mTrendingReposActivityTestRule.launchActivity(intent);
    }

    @Test
    public void checkTitleIsDisplayed() {
        onView(allOf(withText("Trending"), isDisplayed()));
    }

    @Test
    public void checkPopupOptions() {
        ViewInteraction appCompatImageView = onView(allOf(withId(R.id.iv_menu),
                withContentDescription("Menu icon on right top"), isDisplayed()));

        appCompatImageView.perform(click());

        ViewInteraction sortByStars = onView(allOf(withId(R.id.title), withText("Sort by stars"), isDisplayed()));

        sortByStars.perform(click());

        appCompatImageView.perform(click());

        ViewInteraction sortByName = onView(allOf(withId(R.id.title), withText("Sort by name"), isDisplayed()));

        sortByName.perform(click());

    }

    @Test
    public void checkListItemExpansionCollapse() throws Exception {

        // Just hold on for a second
        Thread.sleep(1000);

        // Check if list is displayed
        onView(allOf(withId(R.id.rv_trending_list), isDisplayed()));

        // Wait for little more for adapter to pop results
        Thread.sleep(1000);

        // Check if list item is expanded. We can do that by checking description text visibility
        onView(childAtPosition(childAtPosition(withRecyclerView(R.id.rv_trending_list).atPosition(0), 0),3)).check(doesNotExist());

        // Click on first item
        onView(withRecyclerView(R.id.rv_trending_list).atPosition(0)).perform(click());

        // See if it got expanded
        onView(allOf(childAtPosition(childAtPosition(withRecyclerView(R.id.rv_trending_list).atPosition(0), 0),3), isDisplayed()));

        // Click on same item again
        onView(withRecyclerView(R.id.rv_trending_list).atPosition(0)).perform(click());

        // See if it got collapsed
        onView(childAtPosition(childAtPosition(withRecyclerView(R.id.rv_trending_list).atPosition(0), 0),3)).check(doesNotExist());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private static MockResponse getTrendingRepoMockResponse() {
        MockResponse response = new MockResponse();
        response.setResponseCode(200);
        response.setBody(getTrendingRepoMockResponseString());
        response.setHeader("content-type", "application/json; charset=utf-8");
        response.setHeader("etag", "W/\"52c7-rCMVBmD3AYUD1frabnDonaBHho0\"");
        response.setHeader("cache-control", "max-age=120");
        return response;
    }

    private static String getTrendingRepoMockResponseString() {
        return "[{\"author\":\"Rohit Sharma\",\"name\":\"Deep-Learning-with-TensorFlow-book\",\"avatar\":\"https://github.com/dragen1860.png\",\"url\":\"https://github.com/dragen1860/Deep-Learning-with-TensorFlow-book\",\"description\":\"深度学习开源书，基于TensorFlow 2.0实战。Open source Deep Learning book, based on TensorFlow 2.0 framework.\",\"language\":\"Python\",\"languageColor\":\"#3572A5\",\"stars\":2921,\"forks\":722,\"currentPeriodStars\":1260,\"builtBy\":[{\"username\":\"dragen1860\",\"href\":\"https://github.com/dragen1860\",\"avatar\":\"https://avatars2.githubusercontent.com/u/4252555\"},{\"username\":\"haorenhao\",\"href\":\"https://github.com/haorenhao\",\"avatar\":\"https://avatars1.githubusercontent.com/u/12589674\"},{\"username\":\"justidle2012\",\"href\":\"https://github.com/justidle2012\",\"avatar\":\"https://avatars2.githubusercontent.com/u/14964742\"},{\"username\":\"cclauss\",\"href\":\"https://github.com/cclauss\",\"avatar\":\"https://avatars3.githubusercontent.com/u/3709715\"}]},{\"author\":\"neovim\",\"name\":\"nvim-lsp\",\"avatar\":\"https://github.com/neovim.png\",\"url\":\"https://github.com/neovim/nvim-lsp\",\"description\":\"Common configurations for Neovim Language Servers\",\"language\":\"Lua\",\"languageColor\":\"#000080\",\"stars\":294,\"forks\":13,\"currentPeriodStars\":64,\"builtBy\":[{\"username\":\"norcalli\",\"href\":\"https://github.com/norcalli\",\"avatar\":\"https://avatars3.githubusercontent.com/u/296363\"},{\"username\":\"clason\",\"href\":\"https://github.com/clason\",\"avatar\":\"https://avatars0.githubusercontent.com/u/2361214\"},{\"username\":\"h-michael\",\"href\":\"https://github.com/h-michael\",\"avatar\":\"https://avatars0.githubusercontent.com/u/4556097\"},{\"username\":\"arzg\",\"href\":\"https://github.com/arzg\",\"avatar\":\"https://avatars1.githubusercontent.com/u/31783266\"},{\"username\":\"DanielVoogsgerd\",\"href\":\"https://github.com/DanielVoogsgerd\",\"avatar\":\"https://avatars3.githubusercontent.com/u/1727719\"}]},{\"author\":\"jackfrued\",\"name\":\"Python-100-Days\",\"avatar\":\"https://github.com/jackfrued.png\",\"url\":\"https://github.com/jackfrued/Python-100-Days\",\"description\":\"Python - 100天从新手到大师\",\"language\":\"Jupyter Notebook\",\"languageColor\":\"#DA5B0B\",\"stars\":68920,\"forks\":26614,\"currentPeriodStars\":754,\"builtBy\":[{\"username\":\"jackfrued\",\"href\":\"https://github.com/jackfrued\",\"avatar\":\"https://avatars3.githubusercontent.com/u/7474657\"},{\"username\":\"softpo\",\"href\":\"https://github.com/softpo\",\"avatar\":\"https://avatars3.githubusercontent.com/u/13017944\"},{\"username\":\"Geekya215\",\"href\":\"https://github.com/Geekya215\",\"avatar\":\"https://avatars0.githubusercontent.com/u/10865729\"},{\"username\":\"JalanJiang\",\"href\":\"https://github.com/JalanJiang\",\"avatar\":\"https://avatars1.githubusercontent.com/u/7787716\"},{\"username\":\"jankeromnes\",\"href\":\"https://github.com/jankeromnes\",\"avatar\":\"https://avatars0.githubusercontent.com/u/599268\"}]},{\"author\":\"acidanthera\",\"name\":\"OpenCorePkg\",\"avatar\":\"https://github.com/acidanthera.png\",\"url\":\"https://github.com/acidanthera/OpenCorePkg\",\"description\":\"OpenCore front end\",\"language\":\"C\",\"languageColor\":\"#555555\",\"stars\":672,\"forks\":87,\"currentPeriodStars\":26,\"builtBy\":[{\"username\":\"vit9696\",\"href\":\"https://github.com/vit9696\",\"avatar\":\"https://avatars2.githubusercontent.com/u/4348897\"},{\"username\":\"Andrey1970AppleLife\",\"href\":\"https://github.com/Andrey1970AppleLife\",\"avatar\":\"https://avatars3.githubusercontent.com/u/17758753\"},{\"username\":\"PMheart\",\"href\":\"https://github.com/PMheart\",\"avatar\":\"https://avatars0.githubusercontent.com/u/17109513\"},{\"username\":\"Download-Fritz\",\"href\":\"https://github.com/Download-Fritz\",\"avatar\":\"https://avatars0.githubusercontent.com/u/8659494\"},{\"username\":\"usr-sse2\",\"href\":\"https://github.com/usr-sse2\",\"avatar\":\"https://avatars1.githubusercontent.com/u/446776\"}]},{\"author\":\"sebastianstarke\",\"name\":\"AI4Animation\",\"avatar\":\"https://github.com/sebastianstarke.png\",\"url\":\"https://github.com/sebastianstarke/AI4Animation\",\"description\":\"Bringing Characters to Life with Computer Brains in Unity\",\"language\":\"C#\",\"languageColor\":\"#178600\",\"stars\":1953,\"forks\":285,\"currentPeriodStars\":64,\"builtBy\":[{\"username\":\"sebastianstarke\",\"href\":\"https://github.com/sebastianstarke\",\"avatar\":\"https://avatars1.githubusercontent.com/u/20269775\"},{\"username\":\"doctorpangloss\",\"href\":\"https://github.com/doctorpangloss\",\"avatar\":\"https://avatars3.githubusercontent.com/u/2229300\"}]},{\"author\":\"modin-project\",\"name\":\"modin\",\"avatar\":\"https://github.com/modin-project.png\",\"url\":\"https://github.com/modin-project/modin\",\"description\":\"Modin: Speed up your Pandas workflows by changing a single line of code\",\"language\":\"Python\",\"languageColor\":\"#3572A5\",\"stars\":3283,\"forks\":212,\"currentPeriodStars\":168,\"builtBy\":[{\"username\":\"devin-petersohn\",\"href\":\"https://github.com/devin-petersohn\",\"avatar\":\"https://avatars2.githubusercontent.com/u/10732128\"},{\"username\":\"williamma12\",\"href\":\"https://github.com/williamma12\",\"avatar\":\"https://avatars1.githubusercontent.com/u/12377941\"},{\"username\":\"osalpekar\",\"href\":\"https://github.com/osalpekar\",\"avatar\":\"https://avatars1.githubusercontent.com/u/14008860\"},{\"username\":\"simon-mo\",\"href\":\"https://github.com/simon-mo\",\"avatar\":\"https://avatars2.githubusercontent.com/u/21118851\"},{\"username\":\"eavidan\",\"href\":\"https://github.com/eavidan\",\"avatar\":\"https://avatars1.githubusercontent.com/u/14073228\"}]},{\"author\":\"OpenDiablo2\",\"name\":\"OpenDiablo2\",\"avatar\":\"https://github.com/OpenDiablo2.png\",\"url\":\"https://github.com/OpenDiablo2/OpenDiablo2\",\"description\":\"An open source re-implementation of Diablo 2\",\"language\":\"Go\",\"languageColor\":\"#00ADD8\",\"stars\":2970,\"forks\":206,\"currentPeriodStars\":267,\"builtBy\":[{\"username\":\"essial\",\"href\":\"https://github.com/essial\",\"avatar\":\"https://avatars1.githubusercontent.com/u/242652\"},{\"username\":\"ndechiara\",\"href\":\"https://github.com/ndechiara\",\"avatar\":\"https://avatars2.githubusercontent.com/u/30356480\"},{\"username\":\"Ziemas\",\"href\":\"https://github.com/Ziemas\",\"avatar\":\"https://avatars1.githubusercontent.com/u/5276694\"},{\"username\":\"mewmew\",\"href\":\"https://github.com/mewmew\",\"avatar\":\"https://avatars1.githubusercontent.com/u/1414531\"},{\"username\":\"grazz\",\"href\":\"https://github.com/grazz\",\"avatar\":\"https://avatars1.githubusercontent.com/u/197295\"}]},{\"author\":\"2dust\",\"name\":\"v2rayN\",\"avatar\":\"https://github.com/2dust.png\",\"url\":\"https://github.com/2dust/v2rayN\",\"description\":\"\",\"language\":\"C#\",\"languageColor\":\"#178600\",\"stars\":3212,\"forks\":617,\"currentPeriodStars\":109,\"builtBy\":[{\"username\":\"2dust\",\"href\":\"https://github.com/2dust\",\"avatar\":\"https://avatars1.githubusercontent.com/u/31833384\"}]},{\"author\":\"MoePlayer\",\"name\":\"DPlayer\",\"avatar\":\"https://github.com/MoePlayer.png\",\"url\":\"https://github.com/MoePlayer/DPlayer\",\"description\":\"\uD83C\uDF6D Wow, such a lovely HTML5 danmaku video player\",\"language\":\"JavaScript\",\"languageColor\":\"#f1e05a\",\"stars\":7372,\"forks\":1239,\"currentPeriodStars\":26,\"builtBy\":[{\"username\":\"DIYgod\",\"href\":\"https://github.com/DIYgod\",\"avatar\":\"https://avatars2.githubusercontent.com/u/8266075\"},{\"username\":\"A-Circle-Zhang\",\"href\":\"https://github.com/A-Circle-Zhang\",\"avatar\":\"https://avatars1.githubusercontent.com/u/11173242\"},{\"username\":\"micooz\",\"href\":\"https://github.com/micooz\",\"avatar\":\"https://avatars0.githubusercontent.com/u/8897063\"},{\"username\":\"kn007\",\"href\":\"https://github.com/kn007\",\"avatar\":\"https://avatars3.githubusercontent.com/u/6196903\"}]},{\"author\":\"bannedbook\",\"name\":\"fanqiang\",\"avatar\":\"https://github.com/bannedbook.png\",\"url\":\"https://github.com/bannedbook/fanqiang\",\"description\":\"翻墙-科学上网\",\"language\":\"JavaScript\",\"languageColor\":\"#f1e05a\",\"stars\":13201,\"forks\":3168,\"currentPeriodStars\":60,\"builtBy\":[{\"username\":\"bannedbook\",\"href\":\"https://github.com/bannedbook\",\"avatar\":\"https://avatars2.githubusercontent.com/u/4361923\"}]},{\"author\":\"actions\",\"name\":\"starter-workflows\",\"avatar\":\"https://github.com/actions.png\",\"url\":\"https://github.com/actions/starter-workflows\",\"description\":\"Accelerating new GitHub Actions workflows\",\"stars\":1273,\"forks\":461,\"currentPeriodStars\":67,\"builtBy\":[{\"username\":\"mscoutermarsh\",\"href\":\"https://github.com/mscoutermarsh\",\"avatar\":\"https://avatars1.githubusercontent.com/u/155044\"},{\"username\":\"andymckay\",\"href\":\"https://github.com/andymckay\",\"avatar\":\"https://avatars0.githubusercontent.com/u/74699\"},{\"username\":\"pmarsceill\",\"href\":\"https://github.com/pmarsceill\",\"avatar\":\"https://avatars1.githubusercontent.com/u/896475\"},{\"username\":\"chrispat\",\"href\":\"https://github.com/chrispat\",\"avatar\":\"https://avatars1.githubusercontent.com/u/185122\"},{\"username\":\"damccorm\",\"href\":\"https://github.com/damccorm\",\"avatar\":\"https://avatars3.githubusercontent.com/u/42773683\"}]},{\"author\":\"banach-space\",\"name\":\"llvm-tutor\",\"avatar\":\"https://github.com/banach-space.png\",\"url\":\"https://github.com/banach-space/llvm-tutor\",\"description\":\"A collection of LLVM passes (with tests and build scripts)\",\"language\":\"C++\",\"languageColor\":\"#f34b7d\",\"stars\":349,\"forks\":27,\"currentPeriodStars\":50,\"builtBy\":[{\"username\":\"banach-space\",\"href\":\"https://github.com/banach-space\",\"avatar\":\"https://avatars3.githubusercontent.com/u/2990321\"},{\"username\":\"lanza\",\"href\":\"https://github.com/lanza\",\"avatar\":\"https://avatars1.githubusercontent.com/u/12656790\"},{\"username\":\"wyjw\",\"href\":\"https://github.com/wyjw\",\"avatar\":\"https://avatars0.githubusercontent.com/u/15258136\"}]},{\"author\":\"godotengine\",\"name\":\"godot\",\"avatar\":\"https://github.com/godotengine.png\",\"url\":\"https://github.com/godotengine/godot\",\"description\":\"Godot Engine – Multi-platform 2D and 3D game engine\",\"language\":\"C++\",\"languageColor\":\"#f34b7d\",\"stars\":26004,\"forks\":5004,\"currentPeriodStars\":53,\"builtBy\":[{\"username\":\"akien-mga\",\"href\":\"https://github.com/akien-mga\",\"avatar\":\"https://avatars1.githubusercontent.com/u/4701338\"},{\"username\":\"reduz\",\"href\":\"https://github.com/reduz\",\"avatar\":\"https://avatars1.githubusercontent.com/u/6265307\"},{\"username\":\"neikeq\",\"href\":\"https://github.com/neikeq\",\"avatar\":\"https://avatars3.githubusercontent.com/u/7718100\"},{\"username\":\"Naryosha\",\"href\":\"https://github.com/Naryosha\",\"avatar\":\"https://avatars0.githubusercontent.com/u/43379543\"},{\"username\":\"mhilbrunner\",\"href\":\"https://github.com/mhilbrunner\",\"avatar\":\"https://avatars3.githubusercontent.com/u/1654763\"}]},{\"author\":\"vinceliuice\",\"name\":\"Mojave-gtk-theme\",\"avatar\":\"https://github.com/vinceliuice.png\",\"url\":\"https://github.com/vinceliuice/Mojave-gtk-theme\",\"description\":\"Mojave is a macos Mojave like theme for GTK 3, GTK 2 and Gnome-Shell\",\"language\":\"CSS\",\"languageColor\":\"#563d7c\",\"stars\":561,\"forks\":122,\"currentPeriodStars\":21,\"builtBy\":[{\"username\":\"vinceliuice\",\"href\":\"https://github.com/vinceliuice\",\"avatar\":\"https://avatars3.githubusercontent.com/u/7604295\"},{\"username\":\"haoyet\",\"href\":\"https://github.com/haoyet\",\"avatar\":\"https://avatars3.githubusercontent.com/u/28365438\"},{\"username\":\"sburlyaev\",\"href\":\"https://github.com/sburlyaev\",\"avatar\":\"https://avatars0.githubusercontent.com/u/12219746\"},{\"username\":\"ViniciusGiroto\",\"href\":\"https://github.com/ViniciusGiroto\",\"avatar\":\"https://avatars1.githubusercontent.com/u/9205380\"},{\"username\":\"miy4mori\",\"href\":\"https://github.com/miy4mori\",\"avatar\":\"https://avatars2.githubusercontent.com/u/20637056\"}]},{\"author\":\"Snailclimb\",\"name\":\"JavaGuide\",\"avatar\":\"https://github.com/Snailclimb.png\",\"url\":\"https://github.com/Snailclimb/JavaGuide\",\"description\":\"【Java学习+面试指南】 一份涵盖大部分Java程序员所需要掌握的核心知识。\",\"language\":\"Java\",\"languageColor\":\"#b07219\",\"stars\":61200,\"forks\":20649,\"currentPeriodStars\":481,\"builtBy\":[{\"username\":\"Snailclimb\",\"href\":\"https://github.com/Snailclimb\",\"avatar\":\"https://avatars0.githubusercontent.com/u/29880145\"},{\"username\":\"Goose9527\",\"href\":\"https://github.com/Goose9527\",\"avatar\":\"https://avatars0.githubusercontent.com/u/43314997\"},{\"username\":\"Ryze-Zhao\",\"href\":\"https://github.com/Ryze-Zhao\",\"avatar\":\"https://avatars1.githubusercontent.com/u/38486503\"},{\"username\":\"dongzl\",\"href\":\"https://github.com/dongzl\",\"avatar\":\"https://avatars3.githubusercontent.com/u/5917359\"},{\"username\":\"fanofxiaofeng\",\"href\":\"https://github.com/fanofxiaofeng\",\"avatar\":\"https://avatars3.githubusercontent.com/u/3983683\"}]},{\"author\":\"NVIDIAGameWorks\",\"name\":\"kaolin\",\"avatar\":\"https://github.com/NVIDIAGameWorks.png\",\"url\":\"https://github.com/NVIDIAGameWorks/kaolin\",\"description\":\"A PyTorch Library for Accelerating 3D Deep Learning Research\",\"language\":\"C++\",\"languageColor\":\"#f34b7d\",\"stars\":452,\"forks\":52,\"currentPeriodStars\":130,\"builtBy\":[{\"username\":\"Jean-Francois-Lafleche\",\"href\":\"https://github.com/Jean-Francois-Lafleche\",\"avatar\":\"https://avatars3.githubusercontent.com/u/57650687\"},{\"username\":\"krrish94\",\"href\":\"https://github.com/krrish94\",\"avatar\":\"https://avatars1.githubusercontent.com/u/2322201\"}]},{\"author\":\"fabricjs\",\"name\":\"fabric.js\",\"avatar\":\"https://github.com/fabricjs.png\",\"url\":\"https://github.com/fabricjs/fabric.js\",\"description\":\"Javascript Canvas Library, SVG-to-Canvas (& canvas-to-SVG) Parser\",\"language\":\"JavaScript\",\"languageColor\":\"#f1e05a\",\"stars\":14194,\"forks\":2258,\"currentPeriodStars\":113,\"builtBy\":[{\"username\":\"kangax\",\"href\":\"https://github.com/kangax\",\"avatar\":\"https://avatars2.githubusercontent.com/u/383\"},{\"username\":\"asturur\",\"href\":\"https://github.com/asturur\",\"avatar\":\"https://avatars1.githubusercontent.com/u/1194048\"},{\"username\":\"Kienz\",\"href\":\"https://github.com/Kienz\",\"avatar\":\"https://avatars1.githubusercontent.com/u/954545\"},{\"username\":\"GordoRank\",\"href\":\"https://github.com/GordoRank\",\"avatar\":\"https://avatars3.githubusercontent.com/u/6388079\"},{\"username\":\"inssein\",\"href\":\"https://github.com/inssein\",\"avatar\":\"https://avatars3.githubusercontent.com/u/882228\"}]},{\"author\":\"shadowsocks\",\"name\":\"shadowsocks-windows\",\"avatar\":\"https://github.com/shadowsocks.png\",\"url\":\"https://github.com/shadowsocks/shadowsocks-windows\",\"description\":\"If you want to keep a secret, you must also hide it from yourself.\",\"language\":\"C#\",\"languageColor\":\"#178600\",\"stars\":44838,\"forks\":14596,\"currentPeriodStars\":143,\"builtBy\":[{\"username\":\"clowwindy\",\"href\":\"https://github.com/clowwindy\",\"avatar\":\"https://avatars3.githubusercontent.com/u/1073082\"},{\"username\":\"celeron533\",\"href\":\"https://github.com/celeron533\",\"avatar\":\"https://avatars0.githubusercontent.com/u/3608762\"},{\"username\":\"wongsyrone\",\"href\":\"https://github.com/wongsyrone\",\"avatar\":\"https://avatars3.githubusercontent.com/u/5462232\"},{\"username\":\"Noisyfox\",\"href\":\"https://github.com/Noisyfox\",\"avatar\":\"https://avatars0.githubusercontent.com/u/1537155\"},{\"username\":\"GangZhuo\",\"href\":\"https://github.com/GangZhuo\",\"avatar\":\"https://avatars1.githubusercontent.com/u/1397462\"}]},{\"author\":\"30-seconds\",\"name\":\"30-seconds-of-code\",\"avatar\":\"https://github.com/30-seconds.png\",\"url\":\"https://github.com/30-seconds/30-seconds-of-code\",\"description\":\"A curated collection of useful JavaScript snippets that you can understand in 30 seconds or less.\",\"language\":\"JavaScript\",\"languageColor\":\"#f1e05a\",\"stars\":51920,\"forks\":5868,\"currentPeriodStars\":199,\"builtBy\":[{\"username\":\"Chalarangelo\",\"href\":\"https://github.com/Chalarangelo\",\"avatar\":\"https://avatars0.githubusercontent.com/u/8281875\"},{\"username\":\"30secondsofcode\",\"href\":\"https://github.com/30secondsofcode\",\"avatar\":\"https://avatars1.githubusercontent.com/u/35068020\"},{\"username\":\"fejes713\",\"href\":\"https://github.com/fejes713\",\"avatar\":\"https://avatars0.githubusercontent.com/u/25749162\"},{\"username\":\"flxwu\",\"href\":\"https://github.com/flxwu\",\"avatar\":\"https://avatars1.githubusercontent.com/u/16862997\"},{\"username\":\"kriadmin\",\"href\":\"https://github.com/kriadmin\",\"avatar\":\"https://avatars3.githubusercontent.com/u/31792358\"}]},{\"author\":\"ct-Open-Source\",\"name\":\"tuya-convert\",\"avatar\":\"https://github.com/ct-Open-Source.png\",\"url\":\"https://github.com/ct-Open-Source/tuya-convert\",\"description\":\"A collection of scripts to flash Tuya IoT devices to alternative firmwares\",\"language\":\"Python\",\"languageColor\":\"#3572A5\",\"stars\":907,\"forks\":148,\"currentPeriodStars\":20,\"builtBy\":[{\"username\":\"kueblc\",\"href\":\"https://github.com/kueblc\",\"avatar\":\"https://avatars1.githubusercontent.com/u/553906\"},{\"username\":\"merlinschumacher\",\"href\":\"https://github.com/merlinschumacher\",\"avatar\":\"https://avatars3.githubusercontent.com/u/7475989\"},{\"username\":\"ctandi\",\"href\":\"https://github.com/ctandi\",\"avatar\":\"https://avatars0.githubusercontent.com/u/36304504\"},{\"username\":\"M4dmartig4n\",\"href\":\"https://github.com/M4dmartig4n\",\"avatar\":\"https://avatars3.githubusercontent.com/u/55095476\"},{\"username\":\"behrisch\",\"href\":\"https://github.com/behrisch\",\"avatar\":\"https://avatars1.githubusercontent.com/u/454288\"}]},{\"author\":\"LisaDziuba\",\"name\":\"Awesome-Design-Tools\",\"avatar\":\"https://github.com/LisaDziuba.png\",\"url\":\"https://github.com/LisaDziuba/Awesome-Design-Tools\",\"description\":\"The best design tools and plugins for everything \uD83D\uDC49\",\"language\":\"JavaScript\",\"languageColor\":\"#f1e05a\",\"stars\":17703,\"forks\":1196,\"currentPeriodStars\":62,\"builtBy\":[{\"username\":\"valianka\",\"href\":\"https://github.com/valianka\",\"avatar\":\"https://avatars1.githubusercontent.com/u/35335737\"},{\"username\":\"berezovskycom\",\"href\":\"https://github.com/berezovskycom\",\"avatar\":\"https://avatars0.githubusercontent.com/u/20930153\"},{\"username\":\"LisaDziuba\",\"href\":\"https://github.com/LisaDziuba\",\"avatar\":\"https://avatars0.githubusercontent.com/u/23191656\"},{\"username\":\"ribadima\",\"href\":\"https://github.com/ribadima\",\"avatar\":\"https://avatars1.githubusercontent.com/u/2903936\"},{\"username\":\"GLaDO8\",\"href\":\"https://github.com/GLaDO8\",\"avatar\":\"https://avatars2.githubusercontent.com/u/22849100\"}]},{\"author\":\"tidusjar\",\"name\":\"Ombi\",\"avatar\":\"https://github.com/tidusjar.png\",\"url\":\"https://github.com/tidusjar/Ombi\",\"description\":\"Want a Movie or TV Show on Plex or Emby? Use Ombi!\",\"language\":\"C#\",\"languageColor\":\"#178600\",\"stars\":1789,\"forks\":257,\"currentPeriodStars\":29,\"builtBy\":[{\"username\":\"tidusjar\",\"href\":\"https://github.com/tidusjar\",\"avatar\":\"https://avatars0.githubusercontent.com/u/6642220\"},{\"username\":\"Drewster727\",\"href\":\"https://github.com/Drewster727\",\"avatar\":\"https://avatars2.githubusercontent.com/u/4528753\"},{\"username\":\"anojht\",\"href\":\"https://github.com/anojht\",\"avatar\":\"https://avatars3.githubusercontent.com/u/21053678\"},{\"username\":\"PotatoQuality\",\"href\":\"https://github.com/PotatoQuality\",\"avatar\":\"https://avatars0.githubusercontent.com/u/2944704\"},{\"username\":\"MrTopCat\",\"href\":\"https://github.com/MrTopCat\",\"avatar\":\"https://avatars0.githubusercontent.com/u/774415\"}]},{\"author\":\"Tencent\",\"name\":\"plato\",\"avatar\":\"https://github.com/Tencent.png\",\"url\":\"https://github.com/Tencent/plato\",\"description\":\"腾讯高性能图计算框架Plato\",\"language\":\"C++\",\"languageColor\":\"#f34b7d\",\"stars\":729,\"forks\":109,\"currentPeriodStars\":106,\"builtBy\":[{\"username\":\"xuguruogu\",\"href\":\"https://github.com/xuguruogu\",\"avatar\":\"https://avatars2.githubusercontent.com/u/6092236\"},{\"username\":\"ustcyu\",\"href\":\"https://github.com/ustcyu\",\"avatar\":\"https://avatars0.githubusercontent.com/u/57656474\"},{\"username\":\"slashspirit\",\"href\":\"https://github.com/slashspirit\",\"avatar\":\"https://avatars3.githubusercontent.com/u/265429\"},{\"username\":\"yzhwang\",\"href\":\"https://github.com/yzhwang\",\"avatar\":\"https://avatars1.githubusercontent.com/u/1002405\"},{\"username\":\"skyssj\",\"href\":\"https://github.com/skyssj\",\"avatar\":\"https://avatars3.githubusercontent.com/u/11639695\"}]},{\"author\":\"python\",\"name\":\"cpython\",\"avatar\":\"https://github.com/python.png\",\"url\":\"https://github.com/python/cpython\",\"description\":\"The Python programming language\",\"language\":\"Python\",\"languageColor\":\"#3572A5\",\"stars\":27833,\"forks\":12464,\"currentPeriodStars\":72,\"builtBy\":[{\"username\":\"gvanrossum\",\"href\":\"https://github.com/gvanrossum\",\"avatar\":\"https://avatars1.githubusercontent.com/u/2894642\"},{\"username\":\"benjaminp\",\"href\":\"https://github.com/benjaminp\",\"avatar\":\"https://avatars0.githubusercontent.com/u/219470\"},{\"username\":\"birkenfeld\",\"href\":\"https://github.com/birkenfeld\",\"avatar\":\"https://avatars2.githubusercontent.com/u/144359\"},{\"username\":\"freddrake\",\"href\":\"https://github.com/freddrake\",\"avatar\":\"https://avatars0.githubusercontent.com/u/588792\"},{\"username\":\"vstinner\",\"href\":\"https://github.com/vstinner\",\"avatar\":\"https://avatars1.githubusercontent.com/u/194129\"}]},{\"author\":\"material-components\",\"name\":\"material-components-web-components\",\"avatar\":\"https://github.com/material-components.png\",\"url\":\"https://github.com/material-components/material-components-web-components\",\"description\":\"Material Web Components - Material Design implemented as Web Components\",\"language\":\"TypeScript\",\"languageColor\":\"#2b7489\",\"stars\":1442,\"forks\":222,\"currentPeriodStars\":18,\"builtBy\":[{\"username\":\"e111077\",\"href\":\"https://github.com/e111077\",\"avatar\":\"https://avatars1.githubusercontent.com/u/5981958\"},{\"username\":\"azakus\",\"href\":\"https://github.com/azakus\",\"avatar\":\"https://avatars1.githubusercontent.com/u/46725\"},{\"username\":\"aomarks\",\"href\":\"https://github.com/aomarks\",\"avatar\":\"https://avatars2.githubusercontent.com/u/48894\"},{\"username\":\"sorvell\",\"href\":\"https://github.com/sorvell\",\"avatar\":\"https://avatars1.githubusercontent.com/u/78891\"},{\"username\":\"Dabolus\",\"href\":\"https://github.com/Dabolus\",\"avatar\":\"https://avatars0.githubusercontent.com/u/28671340\"}]}]";
    }

}

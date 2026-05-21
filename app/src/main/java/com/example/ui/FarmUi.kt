package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.FarmViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

enum class ActiveTab {
    Dashboard,
    FishAndSales,
    Expenses
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmAppScreen(viewModel: FarmViewModel) {
    var activeTab by remember { mutableStateOf(ActiveTab.Dashboard) }

    // State collections
    val fishStocks by viewModel.fishInventoryList.collectAsStateWithLifecycle()
    val sales by viewModel.salesList.collectAsStateWithLifecycle()
    val expenses by viewModel.expensesList.collectAsStateWithLifecycle()
    val itemsStock by viewModel.itemInventoryList.collectAsStateWithLifecycle()

    // Form modals
    var showAddFishDialog by remember { mutableStateOf(false) }
    var showAddSaleDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showAddItemDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        val uppercaseSubtitle = when (activeTab) {
                            ActiveTab.Dashboard -> "BUSINESS DASHBOARD"
                            ActiveTab.FishAndSales -> "FISH & SALES TRACKER"
                            ActiveTab.Expenses -> "EXPENSES JOURNAL"
                        }
                        Text(
                            text = uppercaseSubtitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Kashif Aqua Farm",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(TealPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile icon",
                            tint = TealPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == ActiveTab.Dashboard,
                    onClick = { activeTab = ActiveTab.Dashboard },
                    label = { Text("Dashboard") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Dashboard"
                        )
                    },
                    modifier = Modifier.testTag("nav_dashboard")
                )
                NavigationBarItem(
                    selected = activeTab == ActiveTab.FishAndSales,
                    onClick = { activeTab = ActiveTab.FishAndSales },
                    label = { Text("Fish & Sales") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Fish & Sales"
                        )
                    },
                    modifier = Modifier.testTag("nav_fish_sales")
                )
                NavigationBarItem(
                    selected = activeTab == ActiveTab.Expenses,
                    onClick = { activeTab = ActiveTab.Expenses },
                    label = { Text("Expenses") },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Expenses"
                        )
                    },
                    modifier = Modifier.testTag("nav_expenses")
                )
            }
        },
        floatingActionButton = {
            when (activeTab) {
                ActiveTab.FishAndSales -> {
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SmallFloatingActionButton(
                            onClick = { showAddFishDialog = true },
                            containerColor = TealTertiary,
                            contentColor = Color.White,
                            modifier = Modifier.testTag("fab_add_fish")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Fish Stock")
                        }
                        ExtendedFloatingActionButton(
                            text = { Text("Add Sale") },
                            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Add Sale") },
                            onClick = { showAddSaleDialog = true },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            modifier = Modifier.testTag("fab_add_sale")
                        )
                    }
                }
                ActiveTab.Expenses -> {
                    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SmallFloatingActionButton(
                            onClick = { showAddItemDialog = true },
                            containerColor = TealTertiary,
                            contentColor = Color.White,
                            modifier = Modifier.testTag("fab_add_dry_item")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Item Stock")
                        }
                        ExtendedFloatingActionButton(
                            text = { Text("New Expense") },
                            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Add Expense") },
                            onClick = { showAddExpenseDialog = true },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            modifier = Modifier.testTag("fab_add_expense")
                        )
                    }
                }
                ActiveTab.Dashboard -> {
                    // Quick add on dashboard
                    ExtendedFloatingActionButton(
                        text = { Text("Quick Sale") },
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Quick Sale") },
                        onClick = { showAddSaleDialog = true },
                        containerColor = TealPrimary,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("fab_quick_sale")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                ActiveTab.Dashboard -> DashboardScreen(
                    fishStocks = fishStocks,
                    sales = sales,
                    expenses = expenses,
                    onNavigateToTab = { activeTab = it }
                )
                ActiveTab.FishAndSales -> FishAndSalesScreen(
                    fishStocks = fishStocks,
                    sales = sales,
                    onDeleteStock = { viewModel.deleteFishInventory(it) },
                    onDeleteSale = { viewModel.deleteSale(it) }
                )
                ActiveTab.Expenses -> ExpensesScreen(
                    expenses = expenses,
                    itemsStock = itemsStock,
                    onDeleteExpense = { viewModel.deleteExpense(it) },
                    onDeleteItem = { viewModel.deleteItemInventory(it) }
                )
            }

            // --- Form Dialogs ---
            if (showAddFishDialog) {
                AddFishStockDialog(
                    onDismiss = { showAddFishDialog = false },
                    onSave = {
                        viewModel.addFishInventory(it)
                        showAddFishDialog = false
                    }
                )
            }

            if (showAddSaleDialog) {
                AddSaleDialog(
                    fishStocks = fishStocks,
                    onDismiss = { showAddSaleDialog = false },
                    onSave = {
                        viewModel.addSale(it)
                        showAddSaleDialog = false
                    }
                )
            }

            if (showAddExpenseDialog) {
                AddExpenseDialog(
                    onDismiss = { showAddExpenseDialog = false },
                    onSave = {
                        viewModel.addExpense(it)
                        showAddExpenseDialog = false
                    }
                )
            }

            if (showAddItemDialog) {
                AddItemInventoryDialog(
                    onDismiss = { showAddItemDialog = false },
                    onSave = {
                        viewModel.addItemInventory(it)
                        showAddItemDialog = false
                    }
                )
            }
        }
    }
}

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(
    fishStocks: List<FishInventory>,
    sales: List<Sale>,
    expenses: List<Expense>,
    onNavigateToTab: (ActiveTab) -> Unit
) {
    val totalEarnings = sales.sumOf { it.totalEarnings }
    val totalExpenses = expenses.sumOf { it.totalCost }
    val netProfit = totalEarnings - totalExpenses

    // Clean currency display
    fun formatAmt(amount: Double): String {
        return "Rs. " + String.format("%,.2f", amount)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Assalamu Alaikum!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Track your scales, feed, and revenues smoothly.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(TealTertiary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Water logo",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        // Accounts summary Cards (Primary KPI Card)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "FINANCIAL OVERVIEW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    letterSpacing = 1.sp
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("primary_kpi_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = TealPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(TealPrimary, TealSecondary)
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Net Business Earnings",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = formatAmt(netProfit),
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        lineHeight = 32.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White.copy(alpha = 0.2f))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = if (netProfit >= 0) "SURPLUS" else "DEFICIT",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(18.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.12f))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .testTag("card_total_earnings")
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Sales",
                                            color = Color.White.copy(alpha = 0.75f),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = formatAmt(totalEarnings),
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.12f))
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                        .testTag("card_total_expenses")
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Expenses",
                                            color = Color.White.copy(alpha = 0.75f),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = formatAmt(totalExpenses),
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Expenses breakdown visual gauge
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Expense Categories Distribution",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val categories = listOf("Fertilizer", "Feed", "Water", "Medicine", "Other")
                    val totalsByCategory = categories.associateWith { cat ->
                        expenses.filter { it.category.lowercase() == cat.lowercase() }.sumOf { it.totalCost }
                    }

                    if (totalExpenses == 0.0) {
                        Text(
                            text = "No expenses logged yet. Tap expenses to log.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                    } else {
                        totalsByCategory.forEach { (cat, cost) ->
                            val pct = if (totalExpenses > 0) (cost / totalExpenses).toFloat() else 0f
                            val color = when (cat) {
                                "Fertilizer" -> GoldAccent
                                "Feed" -> TealPrimary
                                "Water" -> TealTertiary
                                "Medicine" -> PurpleAccent
                                else -> Color.Gray
                            }

                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = cat, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    Text(text = "${formatAmt(cost)} (${String.format("%.1f", pct * 100)}%)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { pct },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = color,
                                    trackColor = color.copy(alpha = 0.15f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Fish Variety Stats
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fish Varieties In Water",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "View All",
                            color = TealPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onNavigateToTab(ActiveTab.FishAndSales) }
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    if (fishStocks.isEmpty()) {
                        Text(
                            text = "No fish types registered in pond inventory.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        fishStocks.take(4).forEach { fish ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(TealTertiary, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${fish.type} (${fish.variety})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Text(
                                    text = "${fish.count} fish | ${fish.weightKg} kg",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. FISH & SALES SCREEN
// ==========================================
@Composable
fun FishAndSalesScreen(
    fishStocks: List<FishInventory>,
    sales: List<Sale>,
    onDeleteStock: (FishInventory) -> Unit,
    onDeleteSale: (Sale) -> Unit
) {
    var subTabState by remember { mutableStateOf(0) } // 0 = Fish Inventory, 1 = Sales Report

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = subTabState, containerColor = MaterialTheme.colorScheme.surface) {
            Tab(
                selected = subTabState == 0,
                onClick = { subTabState = 0 },
                text = { Text("Fish Stock Inventory") }
            )
            Tab(
                selected = subTabState == 1,
                onClick = { subTabState = 1 },
                text = { Text("Sales Report") }
            )
        }

        if (subTabState == 0) {
            FishInventoryPane(fishStocks = fishStocks, onDelete = onDeleteStock)
        } else {
            SalesReportPane(sales = sales, onDelete = onDeleteSale)
        }
    }
}

@Composable
fun FishInventoryPane(
    fishStocks: List<FishInventory>,
    onDelete: (FishInventory) -> Unit
) {
    if (fishStocks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "No Fish Stock",
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Fish Stock Registered",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tap the circular button at the bottom right to record new active fish counts and varieties.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Active Fish Stocks",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TealSecondary
                )
            }

            items(fishStocks) { fish ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sleek leading circular/rounded container
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TealPrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "variety",
                                tint = TealPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Badge(containerColor = TealPrimary.copy(alpha = 0.15f)) {
                                    Text(text = "Inventory Item", color = TealPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = fish.type,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Variety: ${fish.variety}",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(modifier = Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Quantity: ${fish.count} fish",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TealPrimary
                                )
                                Text(
                                    text = "Estimated Weight: ${fish.weightKg} kg",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TealSecondary
                                )
                            }
                            if (fish.notes.isNotEmpty()) {
                                Text(
                                    text = "Notes: ${fish.notes}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        IconButton(onClick = { onDelete(fish) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ExpenseOrange)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalesReportPane(
    sales: List<Sale>,
    onDelete: (Sale) -> Unit
) {
    val df = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    fun formatAmt(amount: Double): String {
        return "Rs. " + String.format("%,.2f", amount)
    }

    if (sales.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "No Sales",
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Sales Logged",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Record harvest revenues in kilograms using the Add Sale button.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TealPrimary)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "TOTAL EARNINGS REPORT", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatAmt(sales.sumOf { it.totalEarnings }),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            text = "Total Harvest Volume sold: ${String.format("%,.1f", sales.sumOf { it.quantityKg })} kg",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Sales Journal Entries", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "${sales.size} transactions", fontSize = 12.sp, color = Color.Gray)
                }
            }

            items(sales) { sale ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sleek leading container representing sales
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(RevenueGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "sale item",
                                tint = RevenueGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${sale.fishType} (${sale.variety})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = df.format(Date(sale.dateMillisecond)),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Sold: ${sale.quantityKg} kg",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Rate: " + formatAmt(sale.ratePerKg) + " / kg",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = formatAmt(sale.totalEarnings),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = RevenueGreen
                                )
                            }
                            if (sale.notes.isNotEmpty()) {
                                Text(
                                    text = "Notes: ${sale.notes}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        IconButton(onClick = { onDelete(sale) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Sale", tint = ExpenseOrange)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. EXPENSES SCREEN
// ==========================================
@Composable
fun ExpensesScreen(
    expenses: List<Expense>,
    itemsStock: List<ItemInventory>,
    onDeleteExpense: (Expense) -> Unit,
    onDeleteItem: (ItemInventory) -> Unit
) {
    var subTabState by remember { mutableStateOf(0) } // 0 = Expense Journal, 1 = Raw Warehouse Stock

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = subTabState, containerColor = MaterialTheme.colorScheme.surface) {
            Tab(
                selected = subTabState == 0,
                onClick = { subTabState = 0 },
                text = { Text("Expenses Journal") }
            )
            Tab(
                selected = subTabState == 1,
                onClick = { subTabState = 1 },
                text = { Text("Items Inventory") }
            )
        }

        if (subTabState == 0) {
            ExpensesJournalPane(expenses = expenses, onDelete = onDeleteExpense)
        } else {
            WarehouseStockPane(itemsStock = itemsStock, onDelete = onDeleteItem)
        }
    }
}

@Composable
fun ExpensesJournalPane(
    expenses: List<Expense>,
    onDelete: (Expense) -> Unit
) {
    val df = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    fun formatAmt(amount: Double): String {
        return "Rs. " + String.format("%,.2f", amount)
    }

    if (expenses.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "No Expenses",
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Expenses Recorded",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Tap the bottom-right action to log Feed, Fertilizers, Water running bills, or Medicines.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ExpenseOrange)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "TOTAL OUTFLOW / EXPENSES", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatAmt(expenses.sumOf { it.totalCost }),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Purchase & Resource Cost Logs", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "${expenses.size} entries", fontSize = 12.sp, color = Color.Gray)
                }
            }

            items(expenses) { exp ->
                val categoryColor = when (exp.category) {
                    "Fertilizer" -> GoldAccent
                    "Feed" -> TealPrimary
                    "Water" -> TealTertiary
                    "Medicine" -> PurpleAccent
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Beautiful category icon container on left side
                        CategoryIconBox(category = exp.category)
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exp.description,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = df.format(Date(exp.dateMillisecond)),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = exp.category,
                                        fontSize = 11.sp,
                                        color = categoryColor,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    // Custom info based on math
                                    if (exp.bagsCount != null && exp.rate != null) {
                                        Text(
                                            text = "${exp.bagsCount} bags @ " + formatAmt(exp.rate) + "/bag",
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Text(
                                    text = formatAmt(exp.totalCost),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = ExpenseOrange
                                )
                            }
                            if (exp.notes.isNotEmpty()) {
                                Text(
                                    text = "Notes: ${exp.notes}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        IconButton(onClick = { onDelete(exp) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Expense", tint = ExpenseOrange)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WarehouseStockPane(
    itemsStock: List<ItemInventory>,
    onDelete: (ItemInventory) -> Unit
) {
    if (itemsStock.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "No Stock Items",
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Dry Warehouse Empty",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Keep track of feed bags, fertilizers stock level, and medicine supply counts by saving items.",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "On-Hand Dry Warehousing Items",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TealSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(itemsStock) { item ->
                val categoryColor = when (item.category) {
                    "Fertilizer" -> GoldAccent
                    "Feed" -> TealPrimary
                    "Medicine" -> PurpleAccent
                    else -> Color.Gray
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Beautiful category icon container on left side
                        CategoryIconBox(category = item.category)
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.itemName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Badge(containerColor = categoryColor.copy(alpha = 0.2f)) {
                                    Text(text = item.category, color = categoryColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Warehouse Stock Level: ${item.stockCount} ${item.unit}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                            if (item.notes.isNotEmpty()) {
                                Text(
                                    text = "Location/Notes: ${item.notes}",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        IconButton(onClick = { onDelete(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete item", tint = ExpenseOrange)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryIconBox(category: String) {
    val (backColor, iconColor, icon) = when (category.lowercase(Locale.ROOT)) {
        "fertilizer" -> Triple(GoldAccent.copy(alpha = 0.12f), GoldAccent, Icons.Default.Star)
        "feed" -> Triple(TealPrimary.copy(alpha = 0.12f), TealPrimary, Icons.Default.ShoppingCart)
        "water" -> Triple(TealTertiary.copy(alpha = 0.12f), TealTertiary, Icons.Default.Info)
        "medicine" -> Triple(PurpleAccent.copy(alpha = 0.12f), PurpleAccent, Icons.Default.Warning)
        else -> Triple(Color.Gray.copy(alpha = 0.12f), Color.Gray, Icons.AutoMirrored.Filled.List)
    }
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = category,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ==========================================
// 4. DIALOG & FORMS IMPLEMENTATIONS
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFishStockDialog(
    onDismiss: () -> Unit,
    onSave: (FishInventory) -> Unit
) {
    var fishType by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var countText by remember { mutableStateOf("") }
    var weightText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf("") }

    val fishSuggestions = listOf("Grass Carp", "Rohu", "Silver Carp", "Tilapia", "Catla", "Mrigal")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Register Fish Stock",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TealPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = fishType,
                    onValueChange = { fishType = it },
                    label = { Text("Fish Type (e.g. Rohu, Tilapia)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_fish_type"),
                    singleLine = true
                )

                // Quick selector buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    fishSuggestions.take(3).forEach { suggest ->
                        Button(
                            onClick = { fishType = suggest },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Text(text = suggest, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, maxLines = 1)
                        }
                    }
                }

                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("Variety / Size (e.g. Fingerlings, Brood)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_fish_variety"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = countText,
                    onValueChange = { countText = it },
                    label = { Text("Fish Stock Count (Specific Amount)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("input_fish_count"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Pond Weight Amount (In Kilograms)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("input_fish_weight"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Pond No / Water Temp / Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                if (errors.isNotEmpty()) {
                    Text(text = errors, color = ExpenseOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val countVal = countText.toIntOrNull()
                            val weightVal = weightText.toDoubleOrNull()
                            if (fishType.isEmpty() || variety.isEmpty()) {
                                errors = "Please fill Fish Type and Variety."
                            } else if (countVal == null || countVal <= 0) {
                                errors = "Please enter a valid stock size."
                            } else if (weightVal == null || weightVal < 0) {
                                errors = "Please enter estimated weight in Kg."
                            } else {
                                onSave(
                                    FishInventory(
                                        type = fishType,
                                        variety = variety,
                                        count = countVal,
                                        weightKg = weightVal,
                                        notes = notes
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        modifier = Modifier.testTag("submit_fish_stock")
                    ) {
                        Text("Save Stock")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleDialog(
    fishStocks: List<FishInventory>,
    onDismiss: () -> Unit,
    onSave: (Sale) -> Unit
) {
    var selectedFishType by remember { mutableStateOf("") }
    var variety by remember { mutableStateOf("") }
    var rateText by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf("") }

    val computedTotal = (rateText.toDoubleOrNull() ?: 0.0) * (quantityText.toDoubleOrNull() ?: 0.0)

    // Default fish list if empty
    val defaultTypes = listOf("Grass Carp", "Rohu", "Silver Carp", "Tilapia", "Catla")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Fish Sale",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = RevenueGreen,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = selectedFishType,
                    onValueChange = { selectedFishType = it },
                    label = { Text("Fish Type (e.g. Rohu, Tilapia)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_sale_fish_type"),
                    singleLine = true
                )

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("Variety/Size Sold (e.g. Standard, Small)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_sale_variety"),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = { quantityText = it },
                        label = { Text("Quantity (Kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("input_sale_kg"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = rateText,
                        onValueChange = { rateText = it },
                        label = { Text("Rate per Kg") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("input_sale_rate"),
                        singleLine = true
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = RevenueGreen.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Computed Revenue / Earnings:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Rs. " + String.format("%,.2f", computedTotal),
                            color = RevenueGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Buyer Name / Pond Origin / Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                if (errors.isNotEmpty()) {
                    Text(text = errors, color = ExpenseOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val rateVal = rateText.toDoubleOrNull()
                            val qtyVal = quantityText.toDoubleOrNull()

                            if (selectedFishType.isEmpty() || variety.isEmpty()) {
                                errors = "Please select Fish Type and Variety."
                            } else if (qtyVal == null || qtyVal <= 0.0) {
                                errors = "Please enter harvest quantity in kilograms."
                            } else if (rateVal == null || rateVal <= 0.0) {
                                errors = "Please fill in standard rate per kg."
                            } else {
                                onSave(
                                    Sale(
                                        dateMillisecond = System.currentTimeMillis(),
                                        fishType = selectedFishType,
                                        variety = variety,
                                        ratePerKg = rateVal,
                                        quantityKg = qtyVal,
                                        totalEarnings = rateVal * qtyVal,
                                        notes = notes
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RevenueGreen),
                        modifier = Modifier.testTag("submit_sale")
                    ) {
                        Text("Save Sale Entry")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onSave: (Expense) -> Unit
) {
    val categories = listOf("Fertilizer", "Feed", "Water", "Medicine", "Other")
    var selectedCategory by remember { mutableStateOf("Fertilizer") }

    var description by remember { mutableStateOf("") }
    var bagsCountText by remember { mutableStateOf("") }
    var rateText by remember { mutableStateOf("") }
    var flatCostText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf("") }

    // Dropdown helpers
    var categoryExpanded by remember { mutableStateOf(false) }

    val calculatedTotalCost = if (selectedCategory == "Fertilizer" || selectedCategory == "Feed") {
        val bags = bagsCountText.toIntOrNull() ?: 0
        val rate = rateText.toDoubleOrNull() ?: 0.0
        (bags * rate).toDouble()
    } else {
        flatCostText.toDoubleOrNull() ?: 0.0
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Business Expense",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ExpenseOrange,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Category selection chips
                Column {
                    Text(text = "Expense Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = {
                                    selectedCategory = cat
                                    // Reset fields to avoid math conflicts
                                    description = ""
                                    bagsCountText = ""
                                    rateText = ""
                                    flatCostText = ""
                                    errors = ""
                                },
                                label = { Text(cat, fontSize = 10.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Layout depends on category to satisfy specific fields
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = {
                        Text(
                            when (selectedCategory) {
                                "Fertilizer" -> "Fertilizer Type (e.g., Urea, DAP)"
                                "Feed" -> "Feed Mark/Grade (e.g. AquaFeed CP-32)"
                                "Water" -> "Water bill desc (e.g. Pump electricity, diesel)"
                                "Medicine" -> "Medicine desc (e.g. Antibiotics, Lime treatment)"
                                else -> "Expense Details / Purchased Item Name"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().testTag("input_expense_desc"),
                    singleLine = true
                )

                if (selectedCategory == "Fertilizer" || selectedCategory == "Feed") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = bagsCountText,
                            onValueChange = { bagsCountText = it },
                            label = { Text("Number of Bags") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("input_expense_bags"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = rateText,
                            onValueChange = { rateText = it },
                            label = { Text("Rate per Bag") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f).testTag("input_expense_rate"),
                            singleLine = true
                        )
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = ExpenseOrange.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Computed Custom Outflow:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Rs. " + String.format("%,.2f", calculatedTotalCost),
                                color = ExpenseOrange,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = flatCostText,
                        onValueChange = { flatCostText = it },
                        label = { Text("Total Bill/Cost (In Rupees)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("input_expense_cost"),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Store / Receipt No / Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                if (errors.isNotEmpty()) {
                    Text(text = errors, color = ExpenseOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (description.isEmpty()) {
                                errors = "Please fill description/type."
                                return@Button
                            }

                            val isBagBased = selectedCategory == "Fertilizer" || selectedCategory == "Feed"
                            val bags = bagsCountText.toIntOrNull()
                            val rate = rateText.toDoubleOrNull()
                            val flat = flatCostText.toDoubleOrNull()

                            if (isBagBased) {
                                if (bags == null || bags <= 0) {
                                    errors = "Please enter valid number of bags."
                                } else if (rate == null || rate <= 0.0) {
                                    errors = "Please enter standard rate per bag."
                                } else {
                                    onSave(
                                        Expense(
                                            category = selectedCategory,
                                            dateMillisecond = System.currentTimeMillis(),
                                            description = description,
                                            bagsCount = bags,
                                            rate = rate,
                                            totalCost = bags * rate,
                                            notes = notes
                                        )
                                    )
                                }
                            } else {
                                if (flat == null || flat <= 0.0) {
                                    errors = "Please fill in standard bill/cost amount."
                                } else {
                                    onSave(
                                        Expense(
                                            category = selectedCategory,
                                            dateMillisecond = System.currentTimeMillis(),
                                            description = description,
                                            totalCost = flat,
                                            notes = notes
                                        )
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExpenseOrange),
                        modifier = Modifier.testTag("submit_expense")
                    ) {
                        Text("Save Log")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemInventoryDialog(
    onDismiss: () -> Unit,
    onSave: (ItemInventory) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Feed") }
    val categories = listOf("Feed", "Fertilizer", "Medicine", "Other")

    var countText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("Bags") }
    var notes by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf("") }

    val unitsList = listOf("Bags", "Kgs", "Bottles", "Units", "Liters")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Record Depot/Stock Item",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TealPrimary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name (e.g. urea, CP-32 Feed, Lime)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_item_name"),
                    singleLine = true
                )

                Column {
                    Text(text = "Item Category:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = { Text(cat, fontSize = 10.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = countText,
                        onValueChange = { countText = it },
                        label = { Text("Stock Level Volume") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1.2f).testTag("input_item_count"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unit") },
                        modifier = Modifier.weight(0.8f).testTag("input_item_unit"),
                        singleLine = true
                    )
                }

                // Quick unit suggestions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    unitsList.forEach { u ->
                        Button(
                            onClick = { unit = u },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (unit == u) TealPrimary else MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 2.dp)
                        ) {
                            Text(text = u, fontSize = 9.sp, color = if (unit == u) Color.White else MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Shelf / Supplier Details / Location") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )

                if (errors.isNotEmpty()) {
                    Text(text = errors, color = ExpenseOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val countVal = countText.toDoubleOrNull()
                            if (itemName.isEmpty()) {
                                errors = "Please fill in standard Item name."
                            } else if (countVal == null || countVal < 0.0) {
                                errors = "Please enter valid stock amount."
                            } else {
                                onSave(
                                    ItemInventory(
                                        itemName = itemName,
                                        category = selectedCategory,
                                        stockCount = countVal,
                                        unit = unit,
                                        notes = notes
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        modifier = Modifier.testTag("submit_dry_item")
                    ) {
                        Text("Save Item")
                    }
                }
            }
        }
    }
}

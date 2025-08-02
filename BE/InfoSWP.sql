USE [master]
GO
/****** Object:  Database [SWPTEST]    Script Date: 7/13/2025 7:49:35 PM ******/
CREATE DATABASE [SWPTEST]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'SWPTEST', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.TRANQUYET\MSSQL\DATA\SWPTEST.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'SWPTEST_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.TRANQUYET\MSSQL\DATA\SWPTEST_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [SWPTEST] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [SWPTEST].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [SWPTEST] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [SWPTEST] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [SWPTEST] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [SWPTEST] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [SWPTEST] SET ARITHABORT OFF 
GO
ALTER DATABASE [SWPTEST] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [SWPTEST] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [SWPTEST] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [SWPTEST] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [SWPTEST] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [SWPTEST] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [SWPTEST] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [SWPTEST] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [SWPTEST] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [SWPTEST] SET  ENABLE_BROKER 
GO
ALTER DATABASE [SWPTEST] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [SWPTEST] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [SWPTEST] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [SWPTEST] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [SWPTEST] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [SWPTEST] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [SWPTEST] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [SWPTEST] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [SWPTEST] SET  MULTI_USER 
GO
ALTER DATABASE [SWPTEST] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [SWPTEST] SET DB_CHAINING OFF 
GO
ALTER DATABASE [SWPTEST] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [SWPTEST] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [SWPTEST] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [SWPTEST] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [SWPTEST] SET QUERY_STORE = ON
GO
ALTER DATABASE [SWPTEST] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [SWPTEST]
GO
/****** Object:  Table [dbo].[Address]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Address](
	[latitude] [float] NULL,
	[longitude] [float] NULL,
	[address_id] [bigint] IDENTITY(1,1) NOT NULL,
	[ward_id] [bigint] NOT NULL,
	[name_street] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[address_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[apheresis_machine]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[apheresis_machine](
	[is_active] [bit] NULL,
	[last_maintenance] [date] NULL,
	[apheresis_machine_id] [bigint] IDENTITY(1,1) NOT NULL,
	[manufacturer] [varchar](50) NULL,
	[model] [varchar](50) NULL,
	[note] [text] NULL,
	[serial_number] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[apheresis_machine_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK2mqymq9p9yax6y630gk84r2bq] UNIQUE NONCLUSTERED 
(
	[serial_number] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Blog_tags]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Blog_tags](
	[Blog_blogId] [bigint] NOT NULL,
	[tags] [varchar](255) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[blogs]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[blogs](
	[viewCount] [int] NULL,
	[blogId] [bigint] IDENTITY(1,1) NOT NULL,
	[createdAt] [datetime2](6) NOT NULL,
	[updatedAt] [datetime2](6) NULL,
	[user_id] [bigint] NULL,
	[status] [varchar](20) NOT NULL,
	[title] [varchar](200) NOT NULL,
	[content] [text] NOT NULL,
	[thumbnailUrl] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[blogId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[blood_bags]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[blood_bags](
	[hematocrit] [float] NULL,
	[rh] [varchar](2) NOT NULL,
	[volume] [int] NOT NULL,
	[blood_bag_id] [bigint] IDENTITY(1,1) NOT NULL,
	[blood_type_id] [bigint] NULL,
	[collected_at] [datetime2](6) NOT NULL,
	[bag_code] [varchar](20) NOT NULL,
	[donor_id] [varchar](255) NOT NULL,
	[note] [varchar](255) NULL,
	[status] [varchar](255) NOT NULL,
	[test_status] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[blood_bag_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK2saxplgmkw3pybmwqke0rvoni] UNIQUE NONCLUSTERED 
(
	[bag_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodComponentPricing]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodComponentPricing](
	[effectiveDate] [date] NOT NULL,
	[unitPrice] [int] NOT NULL,
	[component_id] [bigint] NOT NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodComponents]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodComponents](
	[ApheresisCompatible] [bit] NULL,
	[IsActive] [bit] NOT NULL,
	[StorageDays] [int] NULL,
	[Blood_Component_ID] [bigint] IDENTITY(1,1) NOT NULL,
	[Code] [varchar](10) NULL,
	[NameBloodComponent] [nvarchar](50) NULL,
	[StorageTemp] [varchar](20) NULL,
	[Type] [varchar](30) NULL,
	[Usage] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[Blood_Component_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKx43n6vweegcjnjhh4rdgf7js] UNIQUE NONCLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodInventory]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodInventory](
	[CriticalThresholdML] [int] NULL,
	[MinThresholdML] [int] NULL,
	[TotalQuantityML] [int] NULL,
	[BloodInventoryID] [bigint] IDENTITY(1,1) NOT NULL,
	[BloodType] [bigint] NULL,
	[ComponentID] [bigint] NULL,
	[ExpiredAt] [datetime2](6) NULL,
	[LastUpdated] [datetime] NULL,
	[created_at] [datetime2](6) NULL,
	[updated_at] [datetime2](6) NULL,
	[Status] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[BloodInventoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodPrices]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodPrices](
	[pricePerBag] [float] NOT NULL,
	[pricePerMl] [float] NULL,
	[blood_type_id] [bigint] NOT NULL,
	[component_id] [bigint] NOT NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKs09o35nl8x0mtd07s1xhoacak] UNIQUE NONCLUSTERED 
(
	[blood_type_id] ASC,
	[component_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodRequests]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodRequests](
	[confirmedVolumeMl] [int] NULL,
	[crossmatchRequired] [bit] NULL,
	[deferredPayment] [bit] NULL,
	[hasAntibodyIssue] [bit] NULL,
	[hasReactionHistory] [bit] NULL,
	[hasTransfusionHistory] [bit] NULL,
	[isPregnant] [bit] NULL,
	[isUnmatched] [bit] NULL,
	[quantityBag] [int] NULL,
	[quantityMl] [int] NULL,
	[totalAmount] [int] NULL,
	[approvedAt] [datetime2](6) NULL,
	[blood_type_id] [bigint] NOT NULL,
	[cancelledAt] [datetime2](6) NULL,
	[codeRedId] [bigint] NULL,
	[component_id] [bigint] NOT NULL,
	[createdAt] [datetime2](6) NULL,
	[doctor_id] [bigint] NOT NULL,
	[expected_blood_type_id] [bigint] NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[neededAt] [datetime2](6) NULL,
	[patient_id] [bigint] NOT NULL,
	[requester_id] [bigint] NOT NULL,
	[updatedAt] [datetime2](6) NULL,
	[cancelReason] [varchar](255) NULL,
	[deferredPaymentReason] [varchar](255) NULL,
	[emergencyNote] [varchar](255) NULL,
	[patientRecordCode] [varchar](255) NULL,
	[payment_status] [varchar](255) NULL,
	[priorityCode] [varchar](255) NULL,
	[reason] [varchar](255) NULL,
	[requestCode] [varchar](255) NULL,
	[requesterName] [varchar](255) NULL,
	[requesterPhone] [varchar](255) NULL,
	[specialNote] [varchar](255) NULL,
	[status] [varchar](255) NULL,
	[triageLevel] [varchar](255) NULL,
	[urgency_level] [varchar](255) NULL,
	[warningNote] [varchar](255) NULL,
	[internalPriorityCode] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodTypes]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodTypes](
	[IsActive] [bit] NOT NULL,
	[BloodTypeID] [bigint] IDENTITY(1,1) NOT NULL,
	[Code] [varchar](10) NULL,
	[Description] [nvarchar](20) NULL,
	[Note] [nvarchar](100) NULL,
	[Rh] [varchar](10) NULL,
PRIMARY KEY CLUSTERED 
(
	[BloodTypeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKsycfdl1yawvow3l672r710mev] UNIQUE NONCLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BloodUnits]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BloodUnits](
	[expiration_date] [date] NULL,
	[quantity_ml] [int] NULL,
	[BloodType] [bigint] NULL,
	[blood_bag_id] [bigint] NULL,
	[component_id] [bigint] NULL,
	[created_at] [datetime2](6) NULL,
	[donation_id] [bigint] NULL,
	[inventory_id] [bigint] NULL,
	[separation_order_id] [bigint] NULL,
	[stored_at] [datetime] NULL,
	[unit_id] [bigint] IDENTITY(1,1) NOT NULL,
	[updated_at] [datetime2](6) NULL,
	[unit_code] [varchar](20) NULL,
	[status] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[unit_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ChatLogs]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ChatLogs](
	[Chat_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[User_Id] [bigint] NULL,
	[created_at] [datetime] NULL,
	[message] [nvarchar](100) NULL,
	[sender] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[Chat_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[City]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[City](
	[city_id] [bigint] IDENTITY(1,1) NOT NULL,
	[name_city] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[city_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[CompatibilityRules]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[CompatibilityRules](
	[is_compatible] [bit] NULL,
	[Rule_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[component_id] [bigint] NULL,
	[donor_type] [bigint] NULL,
	[recipient_type] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[Rule_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[District]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[District](
	[city_id] [bigint] NOT NULL,
	[district_id] [bigint] IDENTITY(1,1) NOT NULL,
	[district_name] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[district_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKf1op319dip83rxuha4qh2rr4q] UNIQUE NONCLUSTERED 
(
	[district_name] ASC,
	[city_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DonationHistories]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DonationHistories](
	[paymentAmount] [int] NULL,
	[paymentCompleted] [bit] NULL,
	[paymentRequired] [bit] NULL,
	[volumeMl] [int] NULL,
	[createdAt] [datetime2](6) NULL,
	[donatedAt] [datetime2](6) NULL,
	[donation_id] [bigint] NULL,
	[donor_id] [bigint] NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[paymentTime] [datetime2](6) NULL,
	[bloodType] [varchar](255) NULL,
	[componentDonated] [varchar](255) NULL,
	[donationLocation] [varchar](255) NULL,
	[paymentMethod] [varchar](255) NULL,
	[status] [varchar](255) NULL,
	[transactionCode] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DonationRegistrations]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DonationRegistrations](
	[is_emergency] [bit] NULL,
	[Registration_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[User_Id] [bigint] NULL,
	[created_at] [datetime2](6) NULL,
	[ready_date] [datetime2](6) NULL,
	[updated_at] [datetime2](6) NULL,
	[Status] [varchar](20) NULL,
	[blood_type] [nvarchar](20) NULL,
	[location] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[Registration_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Donations]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Donations](
	[is_emergency] [bit] NULL,
	[recovered_at] [date] NULL,
	[volume_ml] [int] NULL,
	[Donation_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[User_Id] [bigint] NULL,
	[blood_type] [bigint] NULL,
	[component_id] [bigint] NULL,
	[created_at] [datetime2](6) NULL,
	[donation_time] [datetime2](6) NULL,
	[registration_id] [bigint] NULL,
	[separated_component_id] [bigint] NULL,
	[updated_at] [datetime2](6) NULL,
	[location] [nvarchar](50) NULL,
	[notes] [nvarchar](200) NULL,
	[status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[Donation_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DonorProfiles]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DonorProfiles](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NULL,
	[readiness_level] [varchar](30) NULL,
	[note] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DonorReadinessLog]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DonorReadinessLog](
	[changed_at] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NULL,
	[new_level] [varchar](30) NULL,
	[old_level] [varchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[EmailLogs]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[EmailLogs](
	[User_Id] [bigint] NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[sent_at] [datetime2](6) NULL,
	[body] [ntext] NULL,
	[recipient_email] [varchar](255) NOT NULL,
	[status] [varchar](20) NULL,
	[subject] [nvarchar](255) NULL,
	[type] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HealthCheckFailureLog]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HealthCheckFailureLog](
	[createdAt] [datetime2](6) NULL,
	[logId] [bigint] IDENTITY(1,1) NOT NULL,
	[registration_id] [bigint] NOT NULL,
	[reason] [varchar](255) NULL,
	[staffNote] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[logId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[HealthCheckForms]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[HealthCheckForms](
	[blood_pressure_dia] [int] NULL,
	[blood_pressure_sys] [int] NULL,
	[body_temperature] [float] NULL,
	[had_recent_tattoo_or_surgery] [bit] NULL,
	[has_chronic_illness] [bit] NULL,
	[has_fever] [bit] NULL,
	[has_risky_sexual_behavior] [bit] NULL,
	[heart_rate] [int] NULL,
	[is_eligible] [bit] NULL,
	[is_pregnant_or_breastfeeding] [bit] NULL,
	[took_antibiotics_recently] [bit] NULL,
	[weight_kg] [float] NULL,
	[donation_id] [bigint] NULL,
	[health_check_id] [bigint] IDENTITY(1,1) NOT NULL,
	[registration_id] [bigint] NOT NULL,
	[notes_by_staff] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[health_check_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKaj6o4d2rje2lmolcdel7bw2u2] UNIQUE NONCLUSTERED 
(
	[registration_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[lab_test_results]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[lab_test_results](
	[HBV_Negative] [bit] NULL,
	[HCV_Negative] [bit] NULL,
	[HIV_Negative] [bit] NULL,
	[Malaria_Negative] [bit] NULL,
	[Passed] [bit] NULL,
	[Syphilis_Negative] [bit] NULL,
	[BloodUnitID] [bigint] NULL,
	[LabTestResultID] [bigint] IDENTITY(1,1) NOT NULL,
	[TestedAt] [datetime2](6) NULL,
	[TestedByUserID] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[LabTestResultID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Notifications]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notifications](
	[read] [bit] NULL,
	[NotificationId] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](6) NULL,
	[sent_at] [datetime] NULL,
	[updated_at] [datetime2](6) NULL,
	[user_id] [bigint] NULL,
	[content] [nvarchar](200) NULL,
PRIMARY KEY CLUSTERED 
(
	[NotificationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Patients]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Patients](
	[age] [int] NULL,
	[weight] [float] NULL,
	[createdAt] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[linked_user_id] [bigint] NULL,
	[bloodGroup] [varchar](255) NULL,
	[citizenId] [varchar](255) NULL,
	[fullName] [varchar](255) NULL,
	[gender] [varchar](255) NULL,
	[phone] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PreDonationTests]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PreDonationTests](
	[hbLevel] [float] NULL,
	[hbvResult] [bit] NULL,
	[hcvResult] [bit] NULL,
	[hivResult] [bit] NULL,
	[syphilisResult] [bit] NULL,
	[testDate] [date] NULL,
	[blood_type_id] [bigint] NULL,
	[health_check_id] [bigint] NOT NULL,
	[pre_donation_test_id] [bigint] IDENTITY(1,1) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[pre_donation_test_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKdlap3jmgliwimsiut2tx9p1or] UNIQUE NONCLUSTERED 
(
	[health_check_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ReadinessChangeLog]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ReadinessChangeLog](
	[changedAt] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NULL,
	[from_level] [varchar](255) NULL,
	[to_level] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Reports]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Reports](
	[Report_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime] NULL,
	[generated_by] [bigint] NULL,
	[content] [nvarchar](200) NULL,
	[report_type] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[Report_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RequestLogs]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RequestLogs](
	[BloodRequestId] [bigint] NULL,
	[Timestamp] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[Action] [varchar](50) NULL,
	[PerformedBy] [varchar](100) NULL,
	[Note] [nvarchar](500) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Roles]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Roles](
	[Role_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Role_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK7321kxrp9y29l51cb9k6kxi2u] UNIQUE NONCLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[separation_order]    Script Date: 7/13/2025 7:49:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[separation_order](
	[apheresis_machine_id] [bigint] NULL,
	[blood_bag_id] [bigint] NOT NULL,
	[performed_at] [datetime2](6) NOT NULL,
	[performed_by] [bigint] NULL,
	[separation_order_id] [bigint] IDENTITY(1,1) NOT NULL,
	[PresetVersion] [varchar](255) NULL,
	[note] [text] NULL,
	[separation_method] [varchar](255) NULL,
	[separation_pattern] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[separation_order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SeparationPresetConfig]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SeparationPresetConfig](
	[leukoreduced] [bit] NULL,
	[min_weight] [int] NULL,
	[plasma_ratio] [float] NULL,
	[platelets_fixed] [int] NULL,
	[rbc_ratio] [float] NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[Version] [varchar](255) NULL,
	[gender] [nvarchar](10) NULL,
	[method] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Transfusions]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Transfusions](
	[volume_taken_ml] [int] NULL,
	[Transfusion_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[blood_unit_id] [bigint] NULL,
	[recipient_id] [bigint] NULL,
	[request_id] [bigint] NULL,
	[transfusion_date] [datetime] NULL,
	[approved_by] [nvarchar](50) NULL,
	[notes] [nvarchar](200) NULL,
	[recipient_name] [nvarchar](100) NULL,
	[recipient_phone] [nvarchar](20) NULL,
	[status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[Transfusion_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UrgentDonorContactLog]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UrgentDonorContactLog](
	[blood_request_id] [bigint] NULL,
	[contacted_at] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[user_id] [bigint] NULL,
	[status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UrgentDonorRegistry]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UrgentDonorRegistry](
	[is_available] [bit] NOT NULL,
	[is_verified] [bit] NOT NULL,
	[latitude] [float] NOT NULL,
	[longitude] [float] NOT NULL,
	[address_id] [bigint] NULL,
	[blood_type_id] [bigint] NOT NULL,
	[component_id] [bigint] NULL,
	[created_at] [datetime2](6) NULL,
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[last_contacted] [datetime2](6) NULL,
	[left_group_at] [datetime2](6) NULL,
	[registered_at] [datetime2](6) NOT NULL,
	[user_id] [bigint] NOT NULL,
	[readiness_level] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UrgentRequest]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UrgentRequest](
	[RequestDate] [date] NULL,
	[Units] [int] NOT NULL,
	[UrgentRequest_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[User_Id] [bigint] NULL,
	[BloodType] [nvarchar](20) NULL,
	[HospitalName] [nvarchar](100) NULL,
	[Status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[UrgentRequest_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserProfile]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserProfile](
	[dob] [date] NULL,
	[height] [float] NULL,
	[latitude] [float] NULL,
	[longitude] [float] NULL,
	[recovery_time] [int] NULL,
	[weight] [float] NULL,
	[User_Id] [bigint] NOT NULL,
	[address_id] [bigint] NULL,
	[blood_type_id] [bigint] NULL,
	[created_at] [datetime2](6) NULL,
	[donation_date] [datetime2](6) NULL,
	[citizen_id] [varchar](12) NULL,
	[email] [varchar](100) NULL,
	[full_name] [nvarchar](50) NULL,
	[gender] [nvarchar](10) NULL,
	[location] [nvarchar](100) NULL,
	[note] [nvarchar](255) NULL,
	[occupation] [nvarchar](50) NULL,
	[phone] [varchar](20) NULL,
	[staff_position] [nvarchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[User_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK5ep8b6nrsmnuygdr7nj6brsqx] UNIQUE NONCLUSTERED 
(
	[citizen_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[IsEnable] [bit] NULL,
	[Role_Id] [bigint] NULL,
	[User_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](6) NULL,
	[Email] [varchar](50) NULL,
	[Password] [varchar](255) NOT NULL,
	[UserName] [varchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[User_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKjdfr6kjrxekx1j5vrr77rp44t] UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKoknw43kxme6umk29keb5k8gha] UNIQUE NONCLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[VnPayPayments]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[VnPayPayments](
	[Amount] [numeric](10, 2) NULL,
	[Payment_Id] [bigint] IDENTITY(1,1) NOT NULL,
	[Payment_Time] [datetime] NULL,
	[Request_Id] [bigint] NULL,
	[User_Id] [bigint] NULL,
	[Status] [varchar](20) NOT NULL,
	[Transaction_Code] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[Payment_Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Ward]    Script Date: 7/13/2025 7:49:36 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Ward](
	[latitude] [float] NULL,
	[longitude] [float] NULL,
	[district_id] [bigint] NOT NULL,
	[ward_id] [bigint] IDENTITY(1,1) NOT NULL,
	[ward_name] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[ward_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UKcntgkk7y2fpga0wlovwl2lybm] UNIQUE NONCLUSTERED 
(
	[ward_name] ASC,
	[district_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UKehb9ofwal3m4adjdcq6ro9e2p]    Script Date: 7/13/2025 7:49:36 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UKehb9ofwal3m4adjdcq6ro9e2p] ON [dbo].[BloodUnits]
(
	[unit_code] ASC
)
WHERE ([unit_code] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UK416vmqofacagct8hdsnu05ch7]    Script Date: 7/13/2025 7:49:36 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UK416vmqofacagct8hdsnu05ch7] ON [dbo].[DonorProfiles]
(
	[user_id] ASC
)
WHERE ([user_id] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UK9r6im12rnajh8pp1j4nbnbf50]    Script Date: 7/13/2025 7:49:36 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UK9r6im12rnajh8pp1j4nbnbf50] ON [dbo].[lab_test_results]
(
	[BloodUnitID] ASC
)
WHERE ([BloodUnitID] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UKrqsffqd8sk29u7j2ksd1a1eg]    Script Date: 7/13/2025 7:49:36 PM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UKrqsffqd8sk29u7j2ksd1a1eg] ON [dbo].[Patients]
(
	[linked_user_id] ASC
)
WHERE ([linked_user_id] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BloodTypes] ADD  DEFAULT ((1)) FOR [IsActive]
GO
ALTER TABLE [dbo].[Address]  WITH CHECK ADD  CONSTRAINT [FK3q251qdjola2i2ij4ktpw8lxp] FOREIGN KEY([ward_id])
REFERENCES [dbo].[Ward] ([ward_id])
GO
ALTER TABLE [dbo].[Address] CHECK CONSTRAINT [FK3q251qdjola2i2ij4ktpw8lxp]
GO
ALTER TABLE [dbo].[Blog_tags]  WITH CHECK ADD  CONSTRAINT [FKfjhd8nu7smn13897ggi51lse0] FOREIGN KEY([Blog_blogId])
REFERENCES [dbo].[blogs] ([blogId])
GO
ALTER TABLE [dbo].[Blog_tags] CHECK CONSTRAINT [FKfjhd8nu7smn13897ggi51lse0]
GO
ALTER TABLE [dbo].[blogs]  WITH CHECK ADD  CONSTRAINT [FKa34akjg0ehg6coop3tfvlj55v] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[blogs] CHECK CONSTRAINT [FKa34akjg0ehg6coop3tfvlj55v]
GO
ALTER TABLE [dbo].[blood_bags]  WITH CHECK ADD  CONSTRAINT [FKepran2j9vwuv7kj9u5imxftvn] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[blood_bags] CHECK CONSTRAINT [FKepran2j9vwuv7kj9u5imxftvn]
GO
ALTER TABLE [dbo].[BloodComponentPricing]  WITH CHECK ADD  CONSTRAINT [FKl30g39ssqjtc4gh2884170aqj] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[BloodComponentPricing] CHECK CONSTRAINT [FKl30g39ssqjtc4gh2884170aqj]
GO
ALTER TABLE [dbo].[BloodInventory]  WITH CHECK ADD  CONSTRAINT [FKjvks72auqla2i02ail7w7tpb5] FOREIGN KEY([BloodType])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[BloodInventory] CHECK CONSTRAINT [FKjvks72auqla2i02ail7w7tpb5]
GO
ALTER TABLE [dbo].[BloodInventory]  WITH CHECK ADD  CONSTRAINT [FKlgocjup7iatqmnv1i319u3stj] FOREIGN KEY([ComponentID])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[BloodInventory] CHECK CONSTRAINT [FKlgocjup7iatqmnv1i319u3stj]
GO
ALTER TABLE [dbo].[BloodPrices]  WITH CHECK ADD  CONSTRAINT [FKeybc6frl8olkb6kcq6fc094n6] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[BloodPrices] CHECK CONSTRAINT [FKeybc6frl8olkb6kcq6fc094n6]
GO
ALTER TABLE [dbo].[BloodPrices]  WITH CHECK ADD  CONSTRAINT [FKp13yq1aiveg41k96d2yxukq1k] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[BloodPrices] CHECK CONSTRAINT [FKp13yq1aiveg41k96d2yxukq1k]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FK1i994tk837qg3n0vg9bk2ugy8] FOREIGN KEY([patient_id])
REFERENCES [dbo].[Patients] ([id])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FK1i994tk837qg3n0vg9bk2ugy8]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FKau4sqis9hetn55fthc2a28oqs] FOREIGN KEY([requester_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FKau4sqis9hetn55fthc2a28oqs]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FKeu7j9x4vhrpygab0nlej2or8n] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FKeu7j9x4vhrpygab0nlej2or8n]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FKje46mwopc81gr0qu00ja40lj9] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FKje46mwopc81gr0qu00ja40lj9]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FKl94tylfpheoukjguqwvt3jtp1] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FKl94tylfpheoukjguqwvt3jtp1]
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD  CONSTRAINT [FKpyfbyiagyc5yrk9qw2kxh09qd] FOREIGN KEY([expected_blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[BloodRequests] CHECK CONSTRAINT [FKpyfbyiagyc5yrk9qw2kxh09qd]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FK6fuhjx17soo8i3it7f8gthjx] FOREIGN KEY([donation_id])
REFERENCES [dbo].[Donations] ([Donation_Id])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FK6fuhjx17soo8i3it7f8gthjx]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FK8feb7v4dlj601f32mrvsel00u] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FK8feb7v4dlj601f32mrvsel00u]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FKh621452lg7iq9x4h4dm4642xh] FOREIGN KEY([blood_bag_id])
REFERENCES [dbo].[blood_bags] ([blood_bag_id])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FKh621452lg7iq9x4h4dm4642xh]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FKiuy85rljm0lmqt4n0j5bwc7sm] FOREIGN KEY([inventory_id])
REFERENCES [dbo].[BloodInventory] ([BloodInventoryID])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FKiuy85rljm0lmqt4n0j5bwc7sm]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FKphrlqjl7w49fdi7p9mbtdf1bv] FOREIGN KEY([BloodType])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FKphrlqjl7w49fdi7p9mbtdf1bv]
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD  CONSTRAINT [FKqklrxso554ybjk8o6qno4affx] FOREIGN KEY([separation_order_id])
REFERENCES [dbo].[separation_order] ([separation_order_id])
GO
ALTER TABLE [dbo].[BloodUnits] CHECK CONSTRAINT [FKqklrxso554ybjk8o6qno4affx]
GO
ALTER TABLE [dbo].[ChatLogs]  WITH CHECK ADD  CONSTRAINT [FKopkktvrdcskusofp7eswgwcjc] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[ChatLogs] CHECK CONSTRAINT [FKopkktvrdcskusofp7eswgwcjc]
GO
ALTER TABLE [dbo].[CompatibilityRules]  WITH CHECK ADD  CONSTRAINT [FKa1n7us7rq95v539vy1rkf6sse] FOREIGN KEY([recipient_type])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[CompatibilityRules] CHECK CONSTRAINT [FKa1n7us7rq95v539vy1rkf6sse]
GO
ALTER TABLE [dbo].[CompatibilityRules]  WITH CHECK ADD  CONSTRAINT [FKju9vvlph3t3dtetmjeuiflola] FOREIGN KEY([donor_type])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[CompatibilityRules] CHECK CONSTRAINT [FKju9vvlph3t3dtetmjeuiflola]
GO
ALTER TABLE [dbo].[CompatibilityRules]  WITH CHECK ADD  CONSTRAINT [FKst29rencef4i6dwwujp950cv3] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[CompatibilityRules] CHECK CONSTRAINT [FKst29rencef4i6dwwujp950cv3]
GO
ALTER TABLE [dbo].[District]  WITH CHECK ADD  CONSTRAINT [FK15s3hsw1brx62arcwt09jsqbo] FOREIGN KEY([city_id])
REFERENCES [dbo].[City] ([city_id])
GO
ALTER TABLE [dbo].[District] CHECK CONSTRAINT [FK15s3hsw1brx62arcwt09jsqbo]
GO
ALTER TABLE [dbo].[DonationHistories]  WITH CHECK ADD  CONSTRAINT [FK1e2roexnx5ksj2f3mw5xnpsnw] FOREIGN KEY([donation_id])
REFERENCES [dbo].[Donations] ([Donation_Id])
GO
ALTER TABLE [dbo].[DonationHistories] CHECK CONSTRAINT [FK1e2roexnx5ksj2f3mw5xnpsnw]
GO
ALTER TABLE [dbo].[DonationHistories]  WITH CHECK ADD  CONSTRAINT [FKsfenq3knfl331htafbbmc0on7] FOREIGN KEY([donor_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[DonationHistories] CHECK CONSTRAINT [FKsfenq3knfl331htafbbmc0on7]
GO
ALTER TABLE [dbo].[DonationRegistrations]  WITH CHECK ADD  CONSTRAINT [FKnbifj0rd1rip2j57q2f1020it] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[DonationRegistrations] CHECK CONSTRAINT [FKnbifj0rd1rip2j57q2f1020it]
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD  CONSTRAINT [FK1vmfyr9a7dyiti73jx7xlytn0] FOREIGN KEY([separated_component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[Donations] CHECK CONSTRAINT [FK1vmfyr9a7dyiti73jx7xlytn0]
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD  CONSTRAINT [FK8cfpwpbw4a5kf80sqark1pvsj] FOREIGN KEY([registration_id])
REFERENCES [dbo].[DonationRegistrations] ([Registration_Id])
GO
ALTER TABLE [dbo].[Donations] CHECK CONSTRAINT [FK8cfpwpbw4a5kf80sqark1pvsj]
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD  CONSTRAINT [FKaibf6jx57ewt3viyx0lcbmyc9] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[Donations] CHECK CONSTRAINT [FKaibf6jx57ewt3viyx0lcbmyc9]
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD  CONSTRAINT [FKbcc3vnihfhewklvym654rfpyu] FOREIGN KEY([blood_type])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[Donations] CHECK CONSTRAINT [FKbcc3vnihfhewklvym654rfpyu]
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD  CONSTRAINT [FKrje55h7cd69085i41rddy3lxp] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[Donations] CHECK CONSTRAINT [FKrje55h7cd69085i41rddy3lxp]
GO
ALTER TABLE [dbo].[DonorProfiles]  WITH CHECK ADD  CONSTRAINT [FK932ale09gf31kqjjubkl04hdq] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[DonorProfiles] CHECK CONSTRAINT [FK932ale09gf31kqjjubkl04hdq]
GO
ALTER TABLE [dbo].[DonorReadinessLog]  WITH CHECK ADD  CONSTRAINT [FK41krehfo36xl98ad4wa3b7j9w] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[DonorReadinessLog] CHECK CONSTRAINT [FK41krehfo36xl98ad4wa3b7j9w]
GO
ALTER TABLE [dbo].[EmailLogs]  WITH CHECK ADD  CONSTRAINT [FKqiym6ljfgm6pqv0yef7ak0ajh] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[EmailLogs] CHECK CONSTRAINT [FKqiym6ljfgm6pqv0yef7ak0ajh]
GO
ALTER TABLE [dbo].[HealthCheckFailureLog]  WITH CHECK ADD  CONSTRAINT [FKpb4ngll8japy5wtb0upb29rhw] FOREIGN KEY([registration_id])
REFERENCES [dbo].[DonationRegistrations] ([Registration_Id])
GO
ALTER TABLE [dbo].[HealthCheckFailureLog] CHECK CONSTRAINT [FKpb4ngll8japy5wtb0upb29rhw]
GO
ALTER TABLE [dbo].[HealthCheckForms]  WITH CHECK ADD  CONSTRAINT [FKhsxpq6uln01lkh8cin4jwncwu] FOREIGN KEY([donation_id])
REFERENCES [dbo].[Donations] ([Donation_Id])
GO
ALTER TABLE [dbo].[HealthCheckForms] CHECK CONSTRAINT [FKhsxpq6uln01lkh8cin4jwncwu]
GO
ALTER TABLE [dbo].[HealthCheckForms]  WITH CHECK ADD  CONSTRAINT [FKsvax6fo8fhqxi6prji93uvk52] FOREIGN KEY([registration_id])
REFERENCES [dbo].[DonationRegistrations] ([Registration_Id])
GO
ALTER TABLE [dbo].[HealthCheckForms] CHECK CONSTRAINT [FKsvax6fo8fhqxi6prji93uvk52]
GO
ALTER TABLE [dbo].[lab_test_results]  WITH CHECK ADD  CONSTRAINT [FKou81tnhpn7l46yo8jm6vwwxlc] FOREIGN KEY([BloodUnitID])
REFERENCES [dbo].[BloodUnits] ([unit_id])
GO
ALTER TABLE [dbo].[lab_test_results] CHECK CONSTRAINT [FKou81tnhpn7l46yo8jm6vwwxlc]
GO
ALTER TABLE [dbo].[lab_test_results]  WITH CHECK ADD  CONSTRAINT [FKptcoiu060fssnhsh9new7bnxq] FOREIGN KEY([TestedByUserID])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[lab_test_results] CHECK CONSTRAINT [FKptcoiu060fssnhsh9new7bnxq]
GO
ALTER TABLE [dbo].[Notifications]  WITH CHECK ADD  CONSTRAINT [FKcp0yy59ya9llvvny19fmyesrs] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[Notifications] CHECK CONSTRAINT [FKcp0yy59ya9llvvny19fmyesrs]
GO
ALTER TABLE [dbo].[Patients]  WITH CHECK ADD  CONSTRAINT [FK5bene5h60lmrfrt4uav5f7yms] FOREIGN KEY([linked_user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[Patients] CHECK CONSTRAINT [FK5bene5h60lmrfrt4uav5f7yms]
GO
ALTER TABLE [dbo].[PreDonationTests]  WITH CHECK ADD  CONSTRAINT [FKqudygi9r9o2qe5dkim1kesjc9] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[PreDonationTests] CHECK CONSTRAINT [FKqudygi9r9o2qe5dkim1kesjc9]
GO
ALTER TABLE [dbo].[PreDonationTests]  WITH CHECK ADD  CONSTRAINT [FKr23i8qhioh7s7i8apnjll0yo] FOREIGN KEY([health_check_id])
REFERENCES [dbo].[HealthCheckForms] ([health_check_id])
GO
ALTER TABLE [dbo].[PreDonationTests] CHECK CONSTRAINT [FKr23i8qhioh7s7i8apnjll0yo]
GO
ALTER TABLE [dbo].[ReadinessChangeLog]  WITH CHECK ADD  CONSTRAINT [FKrvirq63j61g3m9qwhfb45gx6o] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[ReadinessChangeLog] CHECK CONSTRAINT [FKrvirq63j61g3m9qwhfb45gx6o]
GO
ALTER TABLE [dbo].[Reports]  WITH CHECK ADD  CONSTRAINT [FKq8r7me64wmtju3ecgoskgciig] FOREIGN KEY([generated_by])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[Reports] CHECK CONSTRAINT [FKq8r7me64wmtju3ecgoskgciig]
GO
ALTER TABLE [dbo].[RequestLogs]  WITH CHECK ADD  CONSTRAINT [FKkg168ej68m46p8s5k587tnulx] FOREIGN KEY([BloodRequestId])
REFERENCES [dbo].[BloodRequests] ([id])
GO
ALTER TABLE [dbo].[RequestLogs] CHECK CONSTRAINT [FKkg168ej68m46p8s5k587tnulx]
GO
ALTER TABLE [dbo].[separation_order]  WITH CHECK ADD  CONSTRAINT [FK3244tkw3i6a2tdwnbduaj7qn9] FOREIGN KEY([blood_bag_id])
REFERENCES [dbo].[blood_bags] ([blood_bag_id])
GO
ALTER TABLE [dbo].[separation_order] CHECK CONSTRAINT [FK3244tkw3i6a2tdwnbduaj7qn9]
GO
ALTER TABLE [dbo].[separation_order]  WITH CHECK ADD  CONSTRAINT [FK4xxrs7uf269mklq37k374wo0e] FOREIGN KEY([apheresis_machine_id])
REFERENCES [dbo].[apheresis_machine] ([apheresis_machine_id])
GO
ALTER TABLE [dbo].[separation_order] CHECK CONSTRAINT [FK4xxrs7uf269mklq37k374wo0e]
GO
ALTER TABLE [dbo].[separation_order]  WITH CHECK ADD  CONSTRAINT [FK7rxtp7btkyum7gpv8vqjnar66] FOREIGN KEY([performed_by])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[separation_order] CHECK CONSTRAINT [FK7rxtp7btkyum7gpv8vqjnar66]
GO
ALTER TABLE [dbo].[Transfusions]  WITH CHECK ADD  CONSTRAINT [FKfdpf3mtby3y4jbgwvxamkhfm] FOREIGN KEY([blood_unit_id])
REFERENCES [dbo].[BloodUnits] ([unit_id])
GO
ALTER TABLE [dbo].[Transfusions] CHECK CONSTRAINT [FKfdpf3mtby3y4jbgwvxamkhfm]
GO
ALTER TABLE [dbo].[Transfusions]  WITH CHECK ADD  CONSTRAINT [FKlwf6nilfkx983o182esro54et] FOREIGN KEY([recipient_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[Transfusions] CHECK CONSTRAINT [FKlwf6nilfkx983o182esro54et]
GO
ALTER TABLE [dbo].[Transfusions]  WITH CHECK ADD  CONSTRAINT [FKr949um98is9wr81amj3dbq2ny] FOREIGN KEY([request_id])
REFERENCES [dbo].[BloodRequests] ([id])
GO
ALTER TABLE [dbo].[Transfusions] CHECK CONSTRAINT [FKr949um98is9wr81amj3dbq2ny]
GO
ALTER TABLE [dbo].[UrgentDonorContactLog]  WITH CHECK ADD  CONSTRAINT [FKldr3kmu4t16nmh79ytchbdkkx] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[UrgentDonorContactLog] CHECK CONSTRAINT [FKldr3kmu4t16nmh79ytchbdkkx]
GO
ALTER TABLE [dbo].[UrgentDonorContactLog]  WITH CHECK ADD  CONSTRAINT [FKsvby3mc2yt4e7yu0t0rx1aj7u] FOREIGN KEY([blood_request_id])
REFERENCES [dbo].[BloodRequests] ([id])
GO
ALTER TABLE [dbo].[UrgentDonorContactLog] CHECK CONSTRAINT [FKsvby3mc2yt4e7yu0t0rx1aj7u]
GO
ALTER TABLE [dbo].[UrgentDonorRegistry]  WITH CHECK ADD  CONSTRAINT [FK362fqnwie0f03ck7vogjlweym] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[UrgentDonorRegistry] CHECK CONSTRAINT [FK362fqnwie0f03ck7vogjlweym]
GO
ALTER TABLE [dbo].[UrgentDonorRegistry]  WITH CHECK ADD  CONSTRAINT [FK6o3p2tulwlchsyuk0hsur0a9p] FOREIGN KEY([component_id])
REFERENCES [dbo].[BloodComponents] ([Blood_Component_ID])
GO
ALTER TABLE [dbo].[UrgentDonorRegistry] CHECK CONSTRAINT [FK6o3p2tulwlchsyuk0hsur0a9p]
GO
ALTER TABLE [dbo].[UrgentDonorRegistry]  WITH CHECK ADD  CONSTRAINT [FKhjganrb4e3l0trv7atbtdwv4r] FOREIGN KEY([address_id])
REFERENCES [dbo].[Address] ([address_id])
GO
ALTER TABLE [dbo].[UrgentDonorRegistry] CHECK CONSTRAINT [FKhjganrb4e3l0trv7atbtdwv4r]
GO
ALTER TABLE [dbo].[UrgentDonorRegistry]  WITH CHECK ADD  CONSTRAINT [FKrpwvwde7tsxfunjf3u2q3eqy2] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[UrgentDonorRegistry] CHECK CONSTRAINT [FKrpwvwde7tsxfunjf3u2q3eqy2]
GO
ALTER TABLE [dbo].[UrgentRequest]  WITH CHECK ADD  CONSTRAINT [FKkp9t4q9fl5og3f1i0i81nipe3] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[UrgentRequest] CHECK CONSTRAINT [FKkp9t4q9fl5og3f1i0i81nipe3]
GO
ALTER TABLE [dbo].[UserProfile]  WITH CHECK ADD  CONSTRAINT [FKbusvk1h9ec98nypm8r6avtmdr] FOREIGN KEY([address_id])
REFERENCES [dbo].[Address] ([address_id])
GO
ALTER TABLE [dbo].[UserProfile] CHECK CONSTRAINT [FKbusvk1h9ec98nypm8r6avtmdr]
GO
ALTER TABLE [dbo].[UserProfile]  WITH CHECK ADD  CONSTRAINT [FKd4ma3x3mfc3jeepja8acfux8k] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[UserProfile] CHECK CONSTRAINT [FKd4ma3x3mfc3jeepja8acfux8k]
GO
ALTER TABLE [dbo].[UserProfile]  WITH CHECK ADD  CONSTRAINT [FKhs57ilx2ql3qmcv4rmsjm66mi] FOREIGN KEY([blood_type_id])
REFERENCES [dbo].[BloodTypes] ([BloodTypeID])
GO
ALTER TABLE [dbo].[UserProfile] CHECK CONSTRAINT [FKhs57ilx2ql3qmcv4rmsjm66mi]
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD  CONSTRAINT [FK6od216o50vuc1sb61bqp7wkw1] FOREIGN KEY([Role_Id])
REFERENCES [dbo].[Roles] ([Role_Id])
GO
ALTER TABLE [dbo].[Users] CHECK CONSTRAINT [FK6od216o50vuc1sb61bqp7wkw1]
GO
ALTER TABLE [dbo].[VnPayPayments]  WITH CHECK ADD  CONSTRAINT [FK6qdbyuh10f0iacy7w2gmxyhn] FOREIGN KEY([User_Id])
REFERENCES [dbo].[Users] ([User_Id])
GO
ALTER TABLE [dbo].[VnPayPayments] CHECK CONSTRAINT [FK6qdbyuh10f0iacy7w2gmxyhn]
GO
ALTER TABLE [dbo].[VnPayPayments]  WITH CHECK ADD  CONSTRAINT [FKew7b43i18uqwla4iyf9gc0jtf] FOREIGN KEY([Request_Id])
REFERENCES [dbo].[BloodRequests] ([id])
GO
ALTER TABLE [dbo].[VnPayPayments] CHECK CONSTRAINT [FKew7b43i18uqwla4iyf9gc0jtf]
GO
ALTER TABLE [dbo].[Ward]  WITH CHECK ADD  CONSTRAINT [FKjpsmudh2tk38jtqh2lqxg6u0y] FOREIGN KEY([district_id])
REFERENCES [dbo].[District] ([district_id])
GO
ALTER TABLE [dbo].[Ward] CHECK CONSTRAINT [FKjpsmudh2tk38jtqh2lqxg6u0y]
GO
ALTER TABLE [dbo].[blogs]  WITH CHECK ADD CHECK  (([status]='ARCHIVED' OR [status]='PUBLISHED' OR [status]='DRAFT'))
GO
ALTER TABLE [dbo].[blood_bags]  WITH CHECK ADD CHECK  (([status]='DISCARDED' OR [status]='SEPARATED' OR [status]='COLLECTED'))
GO
ALTER TABLE [dbo].[blood_bags]  WITH CHECK ADD CHECK  (([test_status]='FAILED' OR [test_status]='PASSED' OR [test_status]='PENDING'))
GO
ALTER TABLE [dbo].[BloodComponents]  WITH CHECK ADD CHECK  (([Type]='WHOLE_BLOOD' OR [Type]='PLATELETS' OR [Type]='PLASMA' OR [Type]='RED_BLOOD_CELLS'))
GO
ALTER TABLE [dbo].[BloodInventory]  WITH CHECK ADD CHECK  (([Status]='EXPIRED' OR [Status]='DISCARDED' OR [Status]='USED' OR [Status]='STORED'))
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD CHECK  (([payment_status]='DEFERRED' OR [payment_status]='WAIVED' OR [payment_status]='REFUNDED' OR [payment_status]='FAILED' OR [payment_status]='SUCCESS' OR [payment_status]='PENDING'))
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD CHECK  (([status]='EXPIRED' OR [status]='PAYMENT_FAILED' OR [status]='WAITING_PAYMENT' OR [status]='WAITING_DONOR' OR [status]='REJECTED' OR [status]='CANCELLED' OR [status]='COMPLETED' OR [status]='APPROVED' OR [status]='PENDING'))
GO
ALTER TABLE [dbo].[BloodRequests]  WITH CHECK ADD CHECK  (([urgency_level]='CAP_CUU' OR [urgency_level]='KHAN_CAP' OR [urgency_level]='BINH_THUONG'))
GO
ALTER TABLE [dbo].[BloodUnits]  WITH CHECK ADD CHECK  (([status]='STORED' OR [status]='FAILED_TEST' OR [status]='EXPIRED' OR [status]='DISCARDED' OR [status]='USED' OR [status]='QUARANTINED' OR [status]='PENDING_TESTING' OR [status]='RESERVED' OR [status]='AVAILABLE'))
GO
ALTER TABLE [dbo].[DonationRegistrations]  WITH CHECK ADD CHECK  (([Status]='FAILED_TEST' OR [Status]='FAILED_HEALTH' OR [Status]='NO_SHOW' OR [Status]='CANCELLED' OR [Status]='DONATED' OR [Status]='CONFIRMED' OR [Status]='PENDING'))
GO
ALTER TABLE [dbo].[Donations]  WITH CHECK ADD CHECK  (([status]='FAILED_TEST' OR [status]='FAILED_HEALTH' OR [status]='NO_SHOW' OR [status]='CANCELLED' OR [status]='DONATED' OR [status]='CONFIRMED' OR [status]='PENDING'))
GO
ALTER TABLE [dbo].[DonorProfiles]  WITH CHECK ADD CHECK  (([readiness_level]='UNDECIDED' OR [readiness_level]='NOT_READY' OR [readiness_level]='EMERGENCY_FLEXIBLE' OR [readiness_level]='EMERGENCY_NOW' OR [readiness_level]='REGULAR'))
GO
ALTER TABLE [dbo].[DonorReadinessLog]  WITH CHECK ADD CHECK  (([new_level]='UNDECIDED' OR [new_level]='NOT_READY' OR [new_level]='EMERGENCY_FLEXIBLE' OR [new_level]='EMERGENCY_NOW' OR [new_level]='REGULAR'))
GO
ALTER TABLE [dbo].[DonorReadinessLog]  WITH CHECK ADD CHECK  (([old_level]='UNDECIDED' OR [old_level]='NOT_READY' OR [old_level]='EMERGENCY_FLEXIBLE' OR [old_level]='EMERGENCY_NOW' OR [old_level]='REGULAR'))
GO
ALTER TABLE [dbo].[ReadinessChangeLog]  WITH CHECK ADD CHECK  (([from_level]='UNDECIDED' OR [from_level]='NOT_READY' OR [from_level]='EMERGENCY_FLEXIBLE' OR [from_level]='EMERGENCY_NOW' OR [from_level]='REGULAR'))
GO
ALTER TABLE [dbo].[ReadinessChangeLog]  WITH CHECK ADD CHECK  (([to_level]='UNDECIDED' OR [to_level]='NOT_READY' OR [to_level]='EMERGENCY_FLEXIBLE' OR [to_level]='EMERGENCY_NOW' OR [to_level]='REGULAR'))
GO
ALTER TABLE [dbo].[Roles]  WITH CHECK ADD CHECK  (([Name]='GUEST' OR [Name]='MEMBER' OR [Name]='STAFF' OR [Name]='ADMIN'))
GO
ALTER TABLE [dbo].[separation_order]  WITH CHECK ADD CHECK  (([separation_method]='FILTRATION' OR [separation_method]='CENTRIFUGE' OR [separation_method]='MACHINE' OR [separation_method]='MANUAL'))
GO
ALTER TABLE [dbo].[separation_order]  WITH CHECK ADD CHECK  (([separation_pattern]='PLASMA_ONLY' OR [separation_pattern]='RBC_PLASMA_PLATELET' OR [separation_pattern]='RBC_PLASMA' OR [separation_pattern]='FULL'))
GO
ALTER TABLE [dbo].[UrgentDonorRegistry]  WITH CHECK ADD CHECK  (([readiness_level]='UNDECIDED' OR [readiness_level]='NOT_READY' OR [readiness_level]='EMERGENCY_FLEXIBLE' OR [readiness_level]='EMERGENCY_NOW' OR [readiness_level]='REGULAR'))
GO
ALTER TABLE [dbo].[UrgentRequest]  WITH CHECK ADD CHECK  (([Status]='EXPIRED' OR [Status]='PAYMENT_FAILED' OR [Status]='WAITING_PAYMENT' OR [Status]='WAITING_DONOR' OR [Status]='REJECTED' OR [Status]='CANCELLED' OR [Status]='COMPLETED' OR [Status]='APPROVED' OR [Status]='PENDING'))
GO
ALTER TABLE [dbo].[VnPayPayments]  WITH CHECK ADD CHECK  (([Status]='DEFERRED' OR [Status]='WAIVED' OR [Status]='REFUNDED' OR [Status]='FAILED' OR [Status]='SUCCESS' OR [Status]='PENDING'))
GO
USE [master]
GO
ALTER DATABASE [SWPTEST] SET  READ_WRITE 
GO

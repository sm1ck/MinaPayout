package ru.janivanov.mina;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import ru.janivanov.mina.obj.Delegator;

public class Main {
	
	public static double TOTAL = 0;
	public static int Coinbase = 720;
	public static List<Delegator> Delegators = new ArrayList<Delegator>();
	public static DecimalFormat Format = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
	public static DecimalFormat Format2 = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
	
	public static double getDouble(String d) {
		return Double.parseDouble(Format.format(Double.parseDouble(d)));
	}
	
	public static double getDouble(double d) {
		return Double.parseDouble(Format.format(d));
	}
	
	public static void main(String[] args) {
		Format.applyPattern("#0.000000000");
		Format.setRoundingMode(RoundingMode.CEILING);
		Format2.applyPattern("#0.000");
		Format2.setRoundingMode(RoundingMode.CEILING);
		Scanner sc = new Scanner(System.in);
		System.out.print("Введите кол-во блоков: ");
		int blocks = Integer.parseInt(sc.nextLine());
		System.out.println("Кол-во блоков: "+blocks);
		while (true) {
			System.out.print("Введите название делегата: ");
			String del = sc.nextLine();
			if (del.equalsIgnoreCase("q")) {
				break;
			}
			System.out.println("Название делегата: "+del);
			System.out.print("Введите кол-во токенов делегата: ");
			double tokens = getDouble(sc.nextLine());
			System.out.println("Кол-во токенов: "+tokens);
			Delegators.add(new Delegator(del, tokens));
		}
		sc.close();
		System.out.println(" ");
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Важно! Все значения выплат округлены до наномин в большую сторону.");
		System.out.println("Всего токенов для выплаты: "+(blocks * Coinbase));
		double total_stake = Delegators.stream().mapToDouble(x -> x.getSum()).sum();
		double total_profit = 0;
		double absolute_profit = 0;
		double total_payout = 0;
		System.out.println("=== Список делегатов ===");
		System.out.println("----------------------------------------------");
		for (Delegator d : Delegators) {
			double provider_share = d.getSum() / total_stake;
			System.out.println(d.getName()+": "+d.getSum()+" ("+Format2.format((provider_share*100))+"%)");
		}
		System.out.println("Всего токенов делегатов: "+getDouble(total_stake));
		System.out.println("----------------------------------------------");
		System.out.println("=== Выплаты делегатам ===");
		System.out.println("----------------------------------------------");
		for (Delegator d : Delegators) {
			double provider_share = d.getSum() / total_stake;
			double payout = getDouble((provider_share * 0.95) * Coinbase * blocks);
			System.out.println(d.getName()+": "+payout+" ("+Format2.format((provider_share*100))+"%)");
			total_payout += payout;
			if (d.getName().toLowerCase().startsWith("me")) {
				absolute_profit += payout;
			}
		}
		System.out.println("Всего: "+Math.ceil(total_payout));
		System.out.println("----------------------------------------------");
		System.out.println("=== Вознаграждение валидатора ===");
		System.out.println("----------------------------------------------");
		for (Delegator d : Delegators) {
			double provider_share = d.getSum() / total_stake;
			double forme = getDouble((provider_share * 0.05) * Coinbase * blocks);
			total_profit += forme;
			absolute_profit += forme;
			System.out.println(d.getName()+": "+forme+" ("+Format2.format((provider_share*100))+"%)");
		}
		System.out.println("Всего: "+total_profit);
		System.out.println("----------------------------------------------");
		System.out.println("Абсолютная прибыль (со своими адресами): "+Math.floor(absolute_profit));
	}

}

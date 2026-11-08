import { useMemo, useState } from 'react';
import {
  Alert, AppBar, Avatar, Box, Button, Card, CardContent, Chip, CircularProgress, Container, Divider,
  FormControl, Grid, IconButton, InputAdornment, InputLabel, MenuItem, Select, Slider, Stack, TextField,
  ToggleButton, ToggleButtonGroup, Toolbar, Tooltip, Typography,
} from '@mui/material';
import {
  AirplanemodeActive, AltRoute, AutoAwesome, CalendarMonth, CheckCircle, CurrencyRupee, DirectionsBus,
  DirectionsCar, Groups, Hotel as HotelIcon, LocationOn, Logout, Star, Train, TravelExplore, WarningAmber,
} from '@mui/icons-material';
import { login, planTrip } from './api';
import type { PlanRequest, TransportMode, TravelPlanResponse, UserProfile } from './types';

const SOURCES = ['Hyderabad', 'Delhi', 'Mumbai', 'Bengaluru', 'Chennai', 'Kolkata'];
const CATEGORY_META: Record<string, { label: string; color: string }> = {
  TRANSPORT: { label: 'Transportation', color: '#0b5563' },
  HOTEL: { label: 'Hotel / Package', color: '#2e86ab' },
  FOOD: { label: 'Food', color: '#e8871e' },
  ACTIVITIES: { label: 'Activities', color: '#7fb069' },
  EMERGENCY_BUFFER: { label: 'Emergency buffer', color: '#9b5de5' },
};
const MODE_ICONS: Record<TransportMode, JSX.Element> = {
  FLIGHT: <AirplanemodeActive fontSize="small" />,
  TRAIN: <Train fontSize="small" />,
  BUS: <DirectionsBus fontSize="small" />,
  CAR: <DirectionsCar fontSize="small" />,
};
const inr = (n: number | null | undefined) => `₹${Math.round(n ?? 0).toLocaleString('en-IN')}`;

function LoginScreen({ onLogin }: { onLogin: (user: UserProfile, token: string) => void }) {
  const [email, setEmail] = useState('demo@nethra.ai');
  const [password, setPassword] = useState('password123');
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const submit = async () => {
    setBusy(true); setError(null);
    try {
      const auth = await login(email, password);
      onLogin(auth.user, auth.token);
    } catch (e: any) {
      setError(`Sign-in failed: ${e.message}. If the services are still starting, wait a few seconds and retry — or continue below.`);
    } finally { setBusy(false); }
  };

  return (
    <Box sx={{ minHeight: '100vh', background: 'linear-gradient(135deg, #073c46 0%, #0b5563 55%, #14666e 100%)', display: 'flex', alignItems: 'center', py: 6 }}>
      <Container maxWidth="sm">
        <Stack spacing={3} alignItems="center">
          <Avatar sx={{ width: 72, height: 72, bgcolor: 'secondary.main' }}><TravelExplore fontSize="large" /></Avatar>
          <Box textAlign="center">
            <Typography variant="h3" color="white">Nethra Travel AI</Typography>
            <Typography variant="h6" sx={{ color: '#ffd9a8', fontWeight: 500, mt: 1 }}>Where you can't see, we help you see.</Typography>
            <Typography sx={{ color: 'rgba(255,255,255,0.82)', mt: 1 }}>
              Tell Nethra your budget, your city and your days — the agents find the trip that honestly fits.
            </Typography>
          </Box>
          <Card sx={{ width: '100%' }}>
            <CardContent sx={{ p: 4 }}>
              <Stack spacing={2.5}>
                <Typography variant="h5">Welcome back</Typography>
                {error && <Alert severity="warning">{error}</Alert>}
                <TextField label="Email" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth autoComplete="email" />
                <TextField label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth autoComplete="current-password"
                  onKeyDown={(e) => { if (e.key === 'Enter') submit(); }} />
                <Button variant="contained" size="large" onClick={submit} disabled={busy} startIcon={busy ? <CircularProgress size={18} color="inherit" /> : <AutoAwesome />}>
                  {busy ? 'Signing in…' : 'Sign in & start planning'}
                </Button>
                <Button variant="text" onClick={() => onLogin({ id: 'guest', name: 'Guest Traveller', email: 'guest@nethra.ai', role: 'USER' }, '')}>
                  Continue without signing in
                </Button>
                <Typography variant="body2" color="text.secondary" textAlign="center">
                  Demo account is prefilled — demo@nethra.ai / password123
                </Typography>
              </Stack>
            </CardContent>
          </Card>
        </Stack>
      </Container>
    </Box>
  );
}

function BreakdownBar({ plan }: { plan: TravelPlanResponse }) {
  const entries = Object.entries(plan.costBreakdown || {});
  const total = Math.max(plan.totalCost, 1);
  return (
    <Stack spacing={1.5}>
      <Box sx={{ display: 'flex', height: 26, borderRadius: 2, overflow: 'hidden', boxShadow: 'inset 0 0 0 1px rgba(0,0,0,0.06)' }}>
        {entries.map(([key, value]) => (
          <Tooltip key={key} title={`${CATEGORY_META[key]?.label ?? key}: ${inr(value)}`}>
            <Box sx={{ width: `${(value / total) * 100}%`, bgcolor: CATEGORY_META[key]?.color ?? '#999' }} />
          </Tooltip>
        ))}
      </Box>
      <Grid container spacing={1}>
        {entries.map(([key, value]) => (
          <Grid item xs={6} sm={4} md={2.4 as any} key={key}>
            <Stack direction="row" spacing={1} alignItems="center">
              <Box sx={{ width: 12, height: 12, borderRadius: 0.5, bgcolor: CATEGORY_META[key]?.color ?? '#999' }} />
              <Box>
                <Typography variant="body2" fontWeight={700}>{inr(value)}</Typography>
                <Typography variant="caption" color="text.secondary">{CATEGORY_META[key]?.label ?? key}</Typography>
              </Box>
            </Stack>
          </Grid>
        ))}
      </Grid>
    </Stack>
  );
}

function RejectionView({ plan, rerun }: { plan: TravelPlanResponse; rerun: (mode: TransportMode) => void }) {
  return (
    <Stack spacing={3}>
      <Card sx={{ borderColor: 'error.main' }}>
        <CardContent sx={{ p: 4 }}>
          <Stack spacing={2}>
            <Stack direction="row" spacing={1.5} alignItems="center">
              <WarningAmber color="error" fontSize="large" />
              <Typography variant="h4">Trip not possible within {inr(plan.request.budgetInr)}</Typography>
            </Stack>
            <Typography variant="h6" color="error.main">
              Minimum cost required: {inr(plan.minimumRequiredBudget)}
            </Typography>
            <Typography color="text.secondary">{plan.reason}</Typography>
            <Divider />
            <Typography variant="subtitle1" fontWeight={700}>Try a different way to travel</Typography>
            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
              {plan.alternativeTransportModes.map((m) => (
                <Chip key={m.transportMode} icon={MODE_ICONS[m.transportMode]} color="primary" variant="outlined"
                  label={`${m.transportMode} — from ${inr(m.minimumRequiredBudget)} (${m.destinationName})`}
                  onClick={() => rerun(m.transportMode)} sx={{ fontWeight: 600 }} />
              ))}
            </Stack>
            {plan.alternativeDestinations.length > 0 && (
              <>
                <Typography variant="subtitle1" fontWeight={700}>Cheapest destinations by {plan.request.transportMode}</Typography>
                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                  {plan.alternativeDestinations.map((d) => (
                    <Chip key={d.destinationId} icon={<LocationOn />} label={`${d.destinationName} — minimum ${inr(d.minimumRequiredBudget)}`} />
                  ))}
                </Stack>
              </>
            )}
            {plan.suggestions.length > 0 && (
              <>
                <Typography variant="subtitle1" fontWeight={700}>What Nethra suggests</Typography>
                <Stack spacing={0.5}>
                  {plan.suggestions.map((s, i) => (
                    <Stack key={i} direction="row" spacing={1} alignItems="flex-start">
                      <AltRoute color="secondary" fontSize="small" sx={{ mt: 0.3 }} />
                      <Typography variant="body2">{s}</Typography>
                    </Stack>
                  ))}
                </Stack>
              </>
            )}
          </Stack>
        </CardContent>
      </Card>
      <AgentTrace plan={plan} />
    </Stack>
  );
}

function AgentTrace({ plan }: { plan: TravelPlanResponse }) {
  if (!plan.agentTrace?.length) return null;
  return (
    <Card>
      <CardContent sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>How the agents decided</Typography>
        <Stack spacing={0}>
          {plan.agentTrace.map((step, i) => (
            <Stack key={i} direction="row" spacing={2}>
              <Stack alignItems="center">
                <Avatar sx={{ width: 30, height: 30, bgcolor: 'primary.main', fontSize: 13 }}>{i + 1}</Avatar>
                {i < plan.agentTrace.length - 1 && <Box sx={{ width: 2, flex: 1, bgcolor: 'divider', my: 0.5 }} />}
              </Stack>
              <Box sx={{ pb: 2.5 }}>
                <Stack direction="row" spacing={1} alignItems="center" flexWrap="wrap" useFlexGap>
                  <Typography fontWeight={700}>{step.agent}</Typography>
                  <Chip size="small" label={step.decision} color={step.decision.includes('REJECT') || step.decision.includes('INFEASIBLE') ? 'error' : 'success'} variant="outlined" />
                </Stack>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>{step.detail}</Typography>
              </Box>
            </Stack>
          ))}
        </Stack>
      </CardContent>
    </Card>
  );
}

function PlanView({ plan }: { plan: TravelPlanResponse }) {
  const d = plan.destination!;
  return (
    <Stack spacing={3}>
      <Card sx={{ background: 'linear-gradient(120deg, #0b5563, #14666e)', color: 'white' }}>
        <CardContent sx={{ p: 4 }}>
          <Stack spacing={1.5}>
            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
              <Chip icon={<CheckCircle />} label="Feasible — inside budget" sx={{ bgcolor: 'success.main', color: 'white', fontWeight: 700 }} />
              <Chip icon={<Star />} label={`${d.rating} rated`} sx={{ bgcolor: 'rgba(255,255,255,0.16)', color: 'white' }} />
              <Chip icon={<CalendarMonth />} label={`${plan.request.days} days`} sx={{ bgcolor: 'rgba(255,255,255,0.16)', color: 'white' }} />
            </Stack>
            <Typography variant="h3">{d.name}</Typography>
            <Typography sx={{ opacity: 0.85 }}>{d.state} · {d.region} · Best in {d.bestMonths.slice(0, 3).join(', ')}</Typography>
            <Typography sx={{ maxWidth: 820 }}>{plan.summary}</Typography>
            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
              {d.tags.map((t) => <Chip key={t} label={t} size="small" sx={{ bgcolor: 'rgba(255,255,255,0.14)', color: 'white' }} />)}
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Card sx={{ height: '100%' }}><CardContent>
            <Stack spacing={1}>
              <Typography variant="overline" color="text.secondary">Transport · {plan.transportOption?.mode}</Typography>
              <Typography variant="h6">{plan.transportOption?.providerName}</Typography>
              <Typography variant="body2" color="text.secondary">
                {plan.transportOption?.sourceCity} → {plan.transportOption?.destinationName} · departs {plan.transportOption?.departureWindow} · {plan.transportOption?.durationHours}h
              </Typography>
              <Typography variant="h5" color="primary.main">{inr(plan.transportOption?.price)}</Typography>
            </Stack>
          </CardContent></Card>
        </Grid>
        <Grid item xs={12} md={4}>
          <Card sx={{ height: '100%' }}><CardContent>
            <Stack spacing={1}>
              <Typography variant="overline" color="text.secondary">{plan.partnerPackage ? 'Partner package (checked first)' : 'Hotel'}</Typography>
              {plan.partnerPackage ? (
                <>
                  <Typography variant="h6">{plan.partnerPackage.partnerPackage.name}</Typography>
                  <Typography variant="body2" color="text.secondary">{plan.partnerPackage.partnerPackage.inclusions.join(' · ')}</Typography>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <Typography variant="h5" color="primary.main">{inr(plan.partnerPackage.quoteTotal)}</Typography>
                    {plan.partnerPackage.estimatedSavings > 0 && <Chip size="small" color="success" label={`Saves ${inr(plan.partnerPackage.estimatedSavings)}`} />}
                  </Stack>
                </>
              ) : plan.hotel ? (
                <>
                  <Typography variant="h6">{plan.hotel.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {plan.hotel.area} · {'★'.repeat(plan.hotel.starTier)} · rated {plan.hotel.rating} · value {plan.hotel.valueScore}
                  </Typography>
                  <Typography variant="h5" color="primary.main">{inr(plan.hotel.pricePerNight)} <Typography component="span" variant="body2" color="text.secondary">/ night</Typography></Typography>
                </>
              ) : <Typography variant="body2">Day trip — no overnight stay needed.</Typography>}
            </Stack>
          </CardContent></Card>
        </Grid>
        <Grid item xs={12} md={4}>
          <Card sx={{ height: '100%' }}><CardContent>
            <Stack spacing={1}>
              <Typography variant="overline" color="text.secondary">Budget outcome</Typography>
              <Typography variant="h6">Total {inr(plan.totalCost)} of {inr(plan.request.budgetInr)}</Typography>
              <Typography variant="body2" color="text.secondary">
                Unspent: {inr(plan.remainingBudget)} · Emergency buffer protected inside the total: {inr(plan.costBreakdown?.EMERGENCY_BUFFER)}
              </Typography>
              <Chip icon={<CheckCircle />} color="success" label="Every category validated under budget" sx={{ alignSelf: 'flex-start' }} />
            </Stack>
          </CardContent></Card>
        </Grid>
      </Grid>

      <Card><CardContent sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>Where the money goes</Typography>
        <BreakdownBar plan={plan} />
      </CardContent></Card>

      {plan.guideHighlights?.length > 0 && (
        <Card><CardContent sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>From the Nethra guide (RAG)</Typography>
          <Stack spacing={0.75}>
            {plan.guideHighlights.map((h, i) => <Typography key={i} variant="body2" color="text.secondary">• {h}</Typography>)}
          </Stack>
        </CardContent></Card>
      )}

      <Box>
        <Typography variant="h5" sx={{ mb: 2 }}>Your day-by-day itinerary</Typography>
        <Grid container spacing={2.5}>
          {plan.itinerary?.days.map((day) => (
            <Grid item xs={12} sm={6} md={4} key={day.dayNumber}>
              <Card sx={{ height: '100%' }}><CardContent>
                <Stack spacing={1.25}>
                  <Stack direction="row" justifyContent="space-between" alignItems="center">
                    <Typography variant="overline" color="secondary.main" fontWeight={800}>Day {day.dayNumber}</Typography>
                    <Chip size="small" label={inr(day.estimatedActivityCost)} variant="outlined" />
                  </Stack>
                  <Typography fontWeight={700}>{day.title}</Typography>
                  <Divider />
                  {day.slots.map((slot, i) => (
                    <Box key={i}>
                      <Stack direction="row" justifyContent="space-between" spacing={1}>
                        <Typography variant="body2" fontWeight={600}>{slot.timeLabel} — {slot.activityName}</Typography>
                        <Typography variant="body2" color="text.secondary">{slot.cost ? inr(slot.cost) : 'Free'}</Typography>
                      </Stack>
                      <Typography variant="body2" color="text.secondary">{slot.description}</Typography>
                    </Box>
                  ))}
                </Stack>
              </CardContent></Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      <AgentTrace plan={plan} />
    </Stack>
  );
}

export default function App() {
  const [user, setUser] = useState<UserProfile | null>(() => {
    try { const raw = localStorage.getItem('nethra-user'); return raw ? JSON.parse(raw) : null; } catch { return null; }
  });
  const [token, setToken] = useState<string>(() => localStorage.getItem('nethra-token') || '');
  const [budget, setBudget] = useState<number>(15000);
  const [source, setSource] = useState('Hyderabad');
  const [days, setDays] = useState(4);
  const [mode, setMode] = useState<TransportMode>('FLIGHT');
  const [travelers, setTravelers] = useState(1);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [plan, setPlan] = useState<TravelPlanResponse | null>(null);

  const request: PlanRequest = useMemo(() => ({ budgetInr: budget, sourceCity: source, days, transportMode: mode, travelers }), [budget, source, days, mode, travelers]);

  const runPlan = async (req: PlanRequest) => {
    setBusy(true); setError(null);
    try { setPlan(await planTrip(req, token || undefined)); }
    catch (e: any) { setError(`Planning failed: ${e.message}. Check that the Nethra services are running, then try again.`); }
    finally { setBusy(false); }
  };

  if (!user) {
    return <LoginScreen onLogin={(u, t) => { setUser(u); setToken(t); localStorage.setItem('nethra-user', JSON.stringify(u)); localStorage.setItem('nethra-token', t); }} />;
  }

  return (
    <Box>
      <AppBar position="sticky" elevation={0} sx={{ bgcolor: '#073c46' }}>
        <Toolbar>
          <Avatar sx={{ bgcolor: 'secondary.main', mr: 1.5 }}><TravelExplore /></Avatar>
          <Box sx={{ flexGrow: 1 }}>
            <Typography variant="h6" sx={{ lineHeight: 1.1 }}>Nethra Travel AI</Typography>
            <Typography variant="caption" sx={{ color: '#ffd9a8' }}>Where you can't see, we help you see.</Typography>
          </Box>
          <Chip avatar={<Avatar>{user.name.charAt(0)}</Avatar>} label={user.name} sx={{ bgcolor: 'rgba(255,255,255,0.14)', color: 'white', mr: 1 }} />
          <IconButton color="inherit" onClick={() => { setUser(null); setToken(''); setPlan(null); localStorage.removeItem('nethra-user'); localStorage.removeItem('nethra-token'); }}>
            <Logout />
          </IconButton>
        </Toolbar>
      </AppBar>

      <Box sx={{ background: 'linear-gradient(120deg, #073c46, #0b5563 60%, #14666e)', color: 'white', pb: 5, pt: 4 }}>
        <Container maxWidth="lg">
          <Typography variant="h4" gutterBottom>Tell us your budget. Nethra finds the trip.</Typography>
          <Typography sx={{ opacity: 0.85, maxWidth: 720 }}>
            No destination needed. Eight agents check feasibility first — if the trip honestly doesn't fit, Nethra says so and shows the minimum, instead of selling you a plan that falls apart.
          </Typography>
        </Container>
      </Box>

      <Container maxWidth="lg" sx={{ mt: -3, pb: 6 }}>
        <Card sx={{ mb: 3 }}>
          <CardContent sx={{ p: 3 }}>
            <Grid container spacing={2.5} alignItems="flex-end">
              <Grid item xs={12} sm={6} md={2.6 as any}>
                <TextField label="Total budget" type="number" value={budget} onChange={(e) => setBudget(Math.max(0, Number(e.target.value)))} fullWidth
                  InputProps={{ startAdornment: <InputAdornment position="start"><CurrencyRupee fontSize="small" /></InputAdornment> }} />
              </Grid>
              <Grid item xs={12} sm={6} md={2.2 as any}>
                <FormControl fullWidth>
                  <InputLabel>Leaving from</InputLabel>
                  <Select label="Leaving from" value={source} onChange={(e) => setSource(e.target.value)}>
                    {SOURCES.map((s) => <MenuItem key={s} value={s}>{s}</MenuItem>)}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} sm={6} md={2.4 as any}>
                <Typography variant="body2" fontWeight={600} gutterBottom>Days: {days}</Typography>
                <Slider value={days} min={1} max={14} step={1} marks valueLabelDisplay="auto" onChange={(_, v) => setDays(v as number)} />
              </Grid>
              <Grid item xs={12} sm={6} md={2 as any}>
                <FormControl fullWidth>
                  <InputLabel>Travellers</InputLabel>
                  <Select label="Travellers" value={travelers} onChange={(e) => setTravelers(Number(e.target.value))}>
                    {[1, 2, 3, 4, 5, 6].map((n) => <MenuItem key={n} value={n}>{n} {n === 1 ? 'person' : 'people'}</MenuItem>)}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12} md={2.8 as any}>
                <ToggleButtonGroup exclusive value={mode} onChange={(_, v) => { if (v) setMode(v); }} fullWidth size="small">
                  {(Object.keys(MODE_ICONS) as TransportMode[]).map((m) => (
                    <ToggleButton key={m} value={m} sx={{ flex: 1 }}>{MODE_ICONS[m]}</ToggleButton>
                  ))}
                </ToggleButtonGroup>
                <Typography variant="caption" color="text.secondary">{mode.charAt(0) + mode.slice(1).toLowerCase()} selected</Typography>
              </Grid>
              <Grid item xs={12}>
                <Button variant="contained" color="secondary" size="large" fullWidth disabled={busy || budget <= 0}
                  startIcon={busy ? <CircularProgress size={18} color="inherit" /> : <AutoAwesome />}
                  onClick={() => runPlan(request)}>
                  {busy ? 'Agents are planning…' : `Plan my ${days}-day trip from ${source}`}
                </Button>
              </Grid>
            </Grid>
          </CardContent>
        </Card>

        {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}
        {plan && (plan.feasible ? <PlanView plan={plan} /> : <RejectionView plan={plan} rerun={(m) => { setMode(m); runPlan({ ...request, transportMode: m }); }} />)}
        {!plan && !error && (
          <Card><CardContent sx={{ p: 4, textAlign: 'center' }}>
            <HotelIcon color="primary" sx={{ fontSize: 40, mb: 1 }} />
            <Typography variant="h6">Try the honesty test</Typography>
            <Typography color="text.secondary">
              The defaults above — ₹15,000, Hyderabad, 4 days, flight — are deliberately impossible. Plan it and watch Nethra refuse, show the true minimum, and offer train and bus alternatives. Then raise the budget or switch modes and watch a full plan appear.
            </Typography>
          </CardContent></Card>
        )}
        <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 4, textAlign: 'center' }}>
          Nethra Travel AI · Mock providers by design in this release — flights, hotels and activities are deterministic simulations behind provider interfaces, ready to swap for live APIs.
        </Typography>
      </Container>
    </Box>
  );
}
